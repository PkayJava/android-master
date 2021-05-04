package ${pkg}.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.view.ViewGroup
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate


import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ${pkg}.common.YuvToRgbConverter
import ${pkg}.theme.BlueprintMasterTheme

import java.util.concurrent.Executors

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalGetImage
@Composable
fun ImageOCRScreen(
        accessId: String,
        secretId: String,
        controller: NavHostController,
        model: ImageOCRScreenModel,
) {

    val scaffoldState = rememberScaffoldState()

    var title = "Jetpack Compose"

    var owner = LocalLifecycleOwner.current

    var context = LocalContext.current

    var dataState = model.state.collectAsState()

    var lunhBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    var lunhText by remember {
        mutableStateOf("")
    }

    val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            model.updateState(state = ImageOCRScreenModel.DataState.Lunh)
        } else {
            model.updateState(state = ImageOCRScreenModel.DataState.Permission)
        }
    }

    if (ContextCompat.checkSelfPermission(LocalContext.current, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
    ) {
        if (dataState.value == ImageOCRScreenModel.DataState.Permission) {
            model.updateState(state = ImageOCRScreenModel.DataState.Lunh)
        }
    } else {
        model.updateState(state = ImageOCRScreenModel.DataState.Permission)
    }

    if (dataState.value is ImageOCRScreenModel.DataState.Lunh) {
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

        var lunhStatus by remember {
            mutableStateOf("Point your camera at a text")
        }

        var lunhAnalyzer by remember {
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
                                val processor = TextRecognition.getClient()
                                processor.process(image)
                                        .addOnSuccessListener { text ->
                                            var lunh = ""
                                            master@ for (block in text.textBlocks) {
                                                for (line in block.lines) {
                                                    for (element in line.elements) {
                                                        var text: String = element.text.trim()
                                                        if (isLuhnNumber(text)) {
                                                            lunh = text
                                                            break@master
                                                        }
                                                    }
                                                }
                                            }
                                            if (lunh == "") {
                                                lunhText = ""
                                                lunhStatus = "Point your camera at a text"
                                            } else {
                                                lunhText = "$lunh"
                                                var bitmapBuffer = Bitmap.createBitmap(
                                                        image.width, image.height, Bitmap.Config.ARGB_8888
                                                )
                                                YuvToRgbConverter(context).yuvToRgb(
                                                        mediaImage,
                                                        bitmapBuffer
                                                )

                                                lunhBitmap = bitmapBuffer

                                                cameraProvider!!.unbindAll()
                                                model.lunhReview()
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
                        TopAppBar(title = { Text(text = title) })
                    },
                    scaffoldState = scaffoldState,
                    snackbarHost = {
                        SnackbarHost(
                                hostState = scaffoldState.snackbarHostState,
                        )
                    },
            ) {
                Box(
                        modifier = Modifier
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
                                                    owner, cameraSelector, preview, lunhAnalyzer
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
                                text = lunhStatus,
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
    } else if (dataState.value is ImageOCRScreenModel.DataState.LunhReview) {
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
                Box(
                        modifier = Modifier
                                .fillMaxSize()
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        var image_width = lunhBitmap!!.width
                        var image_height = lunhBitmap!!.height
                        var ratio = size.height / image_width
                        var new_height = size.width.toInt()
                        var new_width = size.height.toInt()

                        rotate(
                                degrees = 90f,
                                pivot = Offset(0f, 0f)
                        ) {
                            drawImage(
                                    image = lunhBitmap!!.asImageBitmap(),
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
                                text = "Code : $lunhText",
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
    } else if (dataState.value is ImageOCRScreenModel.DataState.Permission) {
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
                val context = LocalContext.current
                Button(
                        onClick = {
                            // Check permission
                            when (PackageManager.PERMISSION_GRANTED) {
                                ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.CAMERA
                                ) -> {
                                    model.updateState(state = ImageOCRScreenModel.DataState.Lunh)
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

fun isLuhnNumber(value: String): Boolean {
    try {
        var sum = 0
        var alternate = false
        for (i in value.length - 1 downTo 0) {
            var n = value.substring(i, i + 1).toInt()
            if (alternate) {
                n *= 2
                if (n > 9) {
                    n = n % 10 + 1
                }
            }
            sum += n
            alternate = !alternate
        }
        return sum % 10 == 0
    } catch (e: NumberFormatException) {
        return false
    }
}