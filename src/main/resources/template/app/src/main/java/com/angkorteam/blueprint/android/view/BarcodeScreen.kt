package ${pkg}.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import ${pkg}.common.YuvToRgbConverter
import ${pkg}.theme.BlueprintMasterTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        if (dataState.value == BarcodeScreenModel.DataState.Permission) {
            model.updateState(state = BarcodeScreenModel.DataState.Barcode)
        }
    } else {
        model.updateState(state = BarcodeScreenModel.DataState.Permission)
    }

    var barcodeStatus by remember {
        mutableStateOf("Point your camera at a barcode/qrcode")
    }

    BlueprintMasterTheme {
        Scaffold(
                topBar = {
                    TopAppBar(title = { Text(text = title) })
                },
                scaffoldState = scaffoldState,
                snackbarHost = {
                    SnackbarHost(
                            hostState = scaffoldState.snackbarHostState,
                    )
                },
        ) {
            when (dataState.value) {
                is BarcodeScreenModel.DataState.Barcode -> {
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
                                                                var code =
                                                                        barcode.substring(0, barcode.length - 1)
                                                                var crc =
                                                                        barcode.substring(barcode.length - 1)
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

                    ConstraintLayout(
                            modifier = Modifier
                                    .fillMaxSize(),
                    ) {

                        var (cameraRef, textRef) = createRefs()

                        AndroidView(
                                modifier = Modifier
                                        .constrainAs(cameraRef) {
                                            start.linkTo(parent.start)
                                            top.linkTo(parent.top)
                                            end.linkTo(parent.end)
                                            bottom.linkTo(parent.bottom)
                                            width = Dimension.fillToConstraints
                                            height = Dimension.fillToConstraints
                                        },
                                factory = { context ->
                                    PreviewView(context).apply {
                                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE

                                        val cameraProviderFuture =
                                                ProcessCameraProvider.getInstance(context)

                                        cameraProviderFuture.addListener(
                                                {
                                                    cameraProvider = cameraProviderFuture.get()
                                                    preview.setSurfaceProvider(surfaceProvider)

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
                                        .constrainAs(textRef) {
                                            start.linkTo(parent.start)
                                            top.linkTo(parent.top)
                                            end.linkTo(parent.end)
                                            width = Dimension.fillToConstraints
                                        }
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
                is BarcodeScreenModel.DataState.BarcodeReview -> {
                    Box(
                            modifier = Modifier
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
                is BarcodeScreenModel.DataState.Permission -> {
                    Button(
                            onClick = {
                                launcher.launch(Manifest.permission.CAMERA)
                            }
                    ) {
                        Text(text = "Check and Request Permission")
                    }
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