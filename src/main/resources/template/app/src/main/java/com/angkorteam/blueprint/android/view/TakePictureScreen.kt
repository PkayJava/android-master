package ${pkg}.view


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import ${pkg}.common.ImageSavedCallback
import ${pkg}.theme.BlueprintMasterTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File
import java.util.concurrent.Executors

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalGetImage
@Composable
fun TakePictureScreen(
        accessId: String,
        secretId: String,
        controller: NavHostController,
        model: TakePictureScreenModel,
) {

    val scaffoldState = rememberScaffoldState()

    var title = "Jetpack Compose"

    var owner = LocalLifecycleOwner.current

    var context = LocalContext.current

    var dataState = model.state.collectAsState()

    var imageBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            model.updateState(state = TakePictureScreenModel.DataState.Picture)
        } else {
            model.updateState(state = TakePictureScreenModel.DataState.Permission)
        }
    }

    if (ContextCompat.checkSelfPermission(LocalContext.current, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
    ) {
        if (dataState.value is TakePictureScreenModel.DataState.Permission) {
            model.updateState(state = TakePictureScreenModel.DataState.Picture)
        }
    } else {
        model.updateState(state = TakePictureScreenModel.DataState.Permission)
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
                is TakePictureScreenModel.DataState.Picture -> {
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

                    var imageCapture by remember {
                        mutableStateOf(ImageCapture.Builder().build())
                    }

                    Box(
                            modifier = Modifier
                                    .fillMaxSize()
                    ) {
                        AndroidView(factory = { context ->
                            PreviewView(context).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                )
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
                                                        owner, cameraSelector, preview, imageCapture
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
                                        .align(alignment = Alignment.BottomCenter)
                                        .background(Color(0x88000000))
                                        .padding(10.dp)
                        ) {
                            Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(onClick = {
                                    var tempFile =
                                            File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
                                    var outputParam =
                                            ImageCapture.OutputFileOptions.Builder(tempFile).build()
                                    imageCapture.takePicture(
                                            outputParam,
                                            Executors.newSingleThreadExecutor(),
                                            ImageSavedCallback(
                                                    imageSaved = { result ->
                                                        imageBitmap =
                                                                BitmapFactory.decodeFile(tempFile.absolutePath)
                                                        tempFile.delete()
                                                        model.imageReview()
                                                    },
                                                    error = { exp ->

                                                    })
                                    )
                                }) {
                                    Text(text = "Take Picture")
                                }
                            }
                        }
                    }
                }
                is TakePictureScreenModel.DataState.PictureReview -> {
                    Box(
                            modifier = Modifier
                                    .fillMaxSize()
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            var image_width = imageBitmap!!.width
                            var image_height = imageBitmap!!.height
                            var ratio = size.height / image_width
                            var new_height = size.width.toInt()
                            var new_width = size.height.toInt()

                            rotate(
                                    degrees = 90f,
                                    pivot = Offset(0f, 0f)
                            ) {
                                drawImage(
                                        image = imageBitmap!!.asImageBitmap(),
                                        srcOffset = IntOffset(0, 0),
                                        dstOffset = IntOffset(0, -new_height),
                                        dstSize = IntSize(new_width, new_height),
                                )
                            }
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
                is TakePictureScreenModel.DataState.Permission -> {
                    Button(
                            onClick = {
                                // Check permission
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
