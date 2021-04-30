package ${pkg}.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import ${pkg}.common.YuvToRgbConverter
import ${pkg}.theme.BlueprintMasterTheme
import ${pkg}.widget.InsetAwareTopAppBar
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ${pkg}.common.DataState
import java.util.concurrent.Executors

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalGetImage
@Composable
fun BarcodeScreen(
    accessId: String,
    secretId: String,
    controller: NavHostController,
    model: BarcodeScreenModel,
) {

    val scaffoldState = rememberScaffoldState()

    var title = "Jetpack Compose"

    var owner = LocalLifecycleOwner.current

    var context = LocalContext.current

    var dataState = model.state.collectAsState()

    var barcodeBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    var barcodeText by remember {
        mutableStateOf("")
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            model.updateState(state = BarcodeScreenModel.DataState.Barcode)
        } else {
            model.updateState(state = BarcodeScreenModel.DataState.Permission)
        }
    }

    if (ContextCompat.checkSelfPermission(LocalContext.current, Manifest.permission.CAMERA) ==
        PackageManager.PERMISSION_GRANTED
    ) {
        model.updateState(state = BarcodeScreenModel.DataState.Barcode)
    } else {
        model.updateState(state = BarcodeScreenModel.DataState.Permission)
    }

    if (dataState.value is BarcodeScreenModel.DataState.Barcode) {
        var cameraProvider by remember {
            mutableStateOf<ProcessCameraProvider?>(null)
        }

        val cameraSelector by remember {
            var cameraSelectorBuilder = CameraSelector.Builder()
            cameraSelectorBuilder.requireLensFacing(CameraSelector.LENS_FACING_BACK)
            mutableStateOf(cameraSelectorBuilder.build())
        }

        var preview by remember {
            val previewBuilder = Preview.Builder()
            mutableStateOf(previewBuilder.build())
        }

        var barcodeStatus by remember {
            mutableStateOf("Point your camera at a barcode/qrcode")
        }

        var barcodeAnalyzer by remember {
            mutableStateOf(ImageAnalysis.Builder().build().apply {
                this.setAnalyzer(
                    Executors.newSingleThreadExecutor(),
                    { imageProxy ->
                        val mediaImage = imageProxy.image
                        if (mediaImage != null) {
                            val image = InputImage.fromMediaImage(
                                mediaImage,
                                imageProxy.imageInfo.rotationDegrees
                            )
                            val processor = BarcodeScanning.getClient()
                            processor.process(image)
                                .addOnSuccessListener { barcodes ->
                                    if (barcodes.size == 1) {
                                        barcodes[0]?.rawValue?.let { barcode ->
                                            var code = barcode.substring(0, barcode.length - 1)
                                            var crc = barcode.substring(barcode.length - 1)
                                            var c = lookupChecksum(code)
                                            if (c == crc) {
                                                var bitmapBuffer = Bitmap.createBitmap(
                                                    image.width,
                                                    image.height,
                                                    Bitmap.Config.ARGB_8888
                                                )
                                                YuvToRgbConverter(context).yuvToRgb(
                                                    mediaImage,
                                                    bitmapBuffer
                                                )

                                                barcodeBitmap = bitmapBuffer

                                                barcodeText = barcode

                                                cameraProvider!!.unbindAll()
                                                model.barcodeReview()
                                            }
                                        }
                                    } else {
                                        barcodeBitmap = null
                                        barcodeStatus = if (barcodes.size > 1) {
                                            "Invalid due to ${barcodes.size} are found"
                                        } else {
                                            "Point your camera at a barcode/qrcode"
                                        }
                                    }
                                    imageProxy.close()
                                }
                                .addOnFailureListener {
                                    imageProxy.close()
                                }
                        }
                    })
            })
        }

        BlueprintMasterTheme {
            Scaffold(
                topBar = {
                    InsetAwareTopAppBar(title = { Text(text = title) })
                },
                scaffoldState = scaffoldState,
                snackbarHost = {
                    SnackbarHost(
                        hostState = scaffoldState.snackbarHostState,
                        modifier = Modifier.navigationBarsWithImePadding()
                    )
                },
            ) {
                Box(
                    modifier = Modifier
                        .navigationBarsPadding(bottom = true)
                        .fillMaxSize()
                ) {
                    AndroidView(factory = { context ->
                        PreviewView(context).apply {
                            var previewView = this
                            this.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            this.implementationMode = PreviewView.ImplementationMode.COMPATIBLE

                            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

                            cameraProviderFuture.addListener(
                                {
                                    cameraProvider = cameraProviderFuture.get()
                                    preview.setSurfaceProvider(previewView.surfaceProvider)

                                    try {
                                        // Unbind use cases before rebinding
                                        cameraProvider!!.unbindAll()

                                        // Bind use cases to camera
                                        cameraProvider!!.bindToLifecycle(
                                            owner, cameraSelector, preview, barcodeAnalyzer
                                        )
                                    } catch (exc: Exception) {

                                    }
                                },
                                ContextCompat.getMainExecutor(context)
                            )
                        }
                    })
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.TopCenter)
                            .background(Color(0x88000000))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = barcodeStatus,
                            color = Color.White,
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    } else if (dataState.value is BarcodeScreenModel.DataState.BarcodeReview) {
        BlueprintMasterTheme {
            Scaffold(
                topBar = {
                    InsetAwareTopAppBar(title = { Text(text = title) })
                },
                scaffoldState = scaffoldState,
                snackbarHost = {
                    SnackbarHost(
                        hostState = scaffoldState.snackbarHostState,
                        modifier = Modifier.navigationBarsWithImePadding()
                    )
                },
            ) {
                Box(
                    modifier = Modifier
                        .navigationBarsPadding(bottom = true)
                        .fillMaxSize()
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        var image_width = barcodeBitmap!!.width
                        var image_height = barcodeBitmap!!.height
                        var ratio = size.height / image_width
                        var new_height = size.width.toInt()
                        var new_width = size.height.toInt()

                        rotate(
                            degrees = 90f,
                            pivot = Offset(0f, 0f)
                        ) {
                            drawImage(
                                image = barcodeBitmap!!.asImageBitmap(),
                                srcOffset = IntOffset(0, 0),
                                dstOffset = IntOffset(0, -new_height),
                                dstSize = IntSize(new_width, new_height),
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.TopCenter)
                            .background(Color(0x88000000))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Code : $barcodeText",
                            color = Color.White,
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.BottomCenter)
                            .background(Color(0x88000000))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(onClick = {
                                val route = "/menu/${accessId}/${secretId}"
                                controller.navigate(route = route)
                            }) {
                                Text(text = "Ok")
                            }
                        }
                    }
                }
            }
        }
    } else if (dataState.value is BarcodeScreenModel.DataState.Permission) {
        BlueprintMasterTheme {
            Scaffold(
                topBar = {
                    InsetAwareTopAppBar(title = { Text(text = title) })
                },
                scaffoldState = scaffoldState,
                snackbarHost = {
                    SnackbarHost(
                        hostState = scaffoldState.snackbarHostState,
                        modifier = Modifier.navigationBarsWithImePadding()
                    )
                },
            ) {
                val context = LocalContext.current
                Button(
                    onClick = {
                        // Check permission
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) -> {
                                model.updateState(state = BarcodeScreenModel.DataState.Barcode)
                            }
                            else -> {
                                // Asking for permission
                                launcher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    }
                ) {
                    Text(text = "Check and Request Permission")
                }
            }
        }
    }

}

fun lookupChecksum(s: CharSequence): String? {
    val length = s.length
    var sum = 0
    var i = length - 1
    while (i >= 0) {
        val digit = s[i] - '0'
        require(!(digit < 0 || digit > 9)) { "barcode" }
        sum += digit
        i -= 2
    }

    sum *= 3
    i = length - 2
    while (i >= 0) {
        val digit = s[i] - '0'
        require(!(digit < 0 || digit > 9)) { "barcode" }
        sum += digit
        i -= 2
    }
    return ((1000 - sum) % 10).toString()
}