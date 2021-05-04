package ${pkg}.view


import android.app.Activity
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.navigation.NavHostController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import ${pkg}.MainActivity
import ${pkg}.R
import ${pkg}.effect.LifecycleEffect
import ${pkg}.effect.ServiceEffect
import ${pkg}.permission.OverlayWindowPermission
import ${pkg}.service.ComposableService
import ${pkg}.theme.BlueprintMasterTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalGetImage
@Composable
fun OverlayWindowScreen(
        accessId: String,
        secretId: String,
        controller: NavHostController,
        model: OverlayWindowScreenModel,
) {

    val scaffoldState = rememberScaffoldState()

    var title = "Jetpack Compose"

    var dataState = model.state.collectAsState()

    var context = LocalContext.current as Activity

    val launcher = rememberLauncherForActivityResult(
            OverlayWindowPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            model.updateState(state = OverlayWindowScreenModel.DataState.HIDE)
        } else {
            model.updateState(state = OverlayWindowScreenModel.DataState.Permission)
        }
    }

    LifecycleEffect { owner, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            if (!OverlayWindowPermission.hasOverlay(context = context)) {
                model.updateState(OverlayWindowScreenModel.DataState.Permission)
            }
        }
    }

    SideEffect {
        if (!OverlayWindowPermission.hasOverlay(context = context)) {
            model.updateState(OverlayWindowScreenModel.DataState.Permission)
        } else {
            if (dataState.value is OverlayWindowScreenModel.DataState.Permission) {
                model.updateState(OverlayWindowScreenModel.DataState.HIDE)
            } else if (dataState.value is OverlayWindowScreenModel.DataState.HIDE) {
                model.updateState(OverlayWindowScreenModel.DataState.HIDE)
            } else if (dataState.value is OverlayWindowScreenModel.DataState.SHOW) {
                model.updateState(OverlayWindowScreenModel.DataState.SHOW)
            }
        }
    }

    val width = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val height = with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx() }

    var lifecycleOwner = LocalContext.current as SavedStateRegistryOwner

    ServiceEffect(
            serviceName = "Overlay",
            serviceClass = ComposableService::class.java,
            onConnected = {
                if (it["POPUP"] == "SHOW") {
                    model.updateState(OverlayWindowScreenModel.DataState.SHOW)
                }
            },
            onDisconnected = {}
    ) { intent, registry ->
        var action = intent.extras?.getString("ACTION")
        if ("SHOW" == action) {
            var windowManager = ContextCompat.getSystemService(context, WindowManager::class.java)
            var overlayView =
                    LayoutInflater.from(context).inflate(R.layout.overlay_window, null, false)
            ViewTreeLifecycleOwner.set(overlayView!!, lifecycleOwner)
            ViewTreeSavedStateRegistryOwner.set(overlayView!!, lifecycleOwner)

            var radio = 16f / 9f
            var w = width / 5f * 3f
            var h = width / radio

            var composeView = overlayView!!.findViewById<ComposeView>(R.id.compose_view)
            composeView.setContent {
                SmallWindowScreen()
            }

            val popupLayoutParams = WindowManager.LayoutParams(
                    w.toInt(), h.toInt(),
                    popupLayoutParamType(),
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                    PixelFormat.TRANSLUCENT
            )
            popupLayoutParams.gravity = Gravity.LEFT or Gravity.TOP
            popupLayoutParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE

            var centerX = (width / 2f) - (w / 2f)
            var centerY = (height / 2f) - (h / 2f)

            popupLayoutParams.x = centerX.toInt()
            popupLayoutParams.y = centerY.toInt()

            Objects.requireNonNull(windowManager)?.addView(overlayView, popupLayoutParams)
            model.updateState(OverlayWindowScreenModel.DataState.SHOW)
            registry["POPUP"] = "SHOW"
            registry["VIEW"] = overlayView
        } else if ("HIDE" == action) {
            var overlayView = registry["VIEW"] as View
            var windowManager = ContextCompat.getSystemService(context, WindowManager::class.java)
            Objects.requireNonNull(windowManager)?.removeView(overlayView)
            model.updateState(OverlayWindowScreenModel.DataState.HIDE)
            registry["POPUP"] = "HIDE"
            registry.remove("VIEW")
        }
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
                is OverlayWindowScreenModel.DataState.SHOW, OverlayWindowScreenModel.DataState.HIDE -> {
                    Box(
                            modifier = Modifier
                                    .fillMaxSize()
                    ) {
                        Box(
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .align(alignment = Alignment.TopCenter)
                                        .background(Color(0x88000000))
                                        .padding(10.dp)
                        ) {
                            Image(
                                    painter = painterResource(id = R.drawable.picture_in_picture),
                                    contentDescription = ""
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
                                if (dataState.value is OverlayWindowScreenModel.DataState.HIDE) {
                                    Button(onClick = {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            var intent =
                                                    Intent(context, ComposableService::class.java)
                                            intent.putExtra(ComposableService.NAME, "Overlay")
                                            intent.putExtra("ACTION", "SHOW")
                                            context.startService(intent)
                                        }
                                    }) {
                                        Text(text = "Show")
                                    }
                                } else if (dataState.value is OverlayWindowScreenModel.DataState.SHOW) {
                                    Button(onClick = {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            var intent =
                                                    Intent(context, ComposableService::class.java)
                                            intent.putExtra(ComposableService.NAME, "Overlay")
                                            intent.putExtra("ACTION", "HIDE")
                                            context.startService(intent)
                                        }
                                    }) {
                                        Text(text = "Hide")
                                    }
                                }
                            }
                        }
                    }
                }
                is OverlayWindowScreenModel.DataState.Permission -> {
                    Box(
                            modifier = Modifier
                                    .fillMaxSize()
                    ) {
                        Box(
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .align(alignment = Alignment.TopCenter)
                                        .background(Color(0x88000000))
                                        .padding(10.dp)
                        ) {
                            Image(
                                    painter = painterResource(id = R.drawable.picture_in_picture),
                                    contentDescription = ""
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
                                    launcher.launch("")
                                }) {
                                    Text(text = "Request Permission")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun SmallWindowScreen() {
    var context = LocalContext.current

    var state by remember {
        mutableStateOf("")
    }

    ServiceEffect(
            serviceName = "OverlayService",
            serviceClass = ComposableService::class.java,
            onConnected = {
            },
            onDisconnected = { /*TODO*/ }) { intent, registry ->
        val event = intent.getStringExtra("APP")
        state = event!!
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
                painter = painterResource(id = R.drawable.picture_in_picture),
                contentDescription = "",
        )
        if (state == "Background") {
            Button(
                    modifier = Modifier.align(alignment = Alignment.Center),
                    onClick = {
                        var intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    }) {
                Text(text = "Open")
            }
        }
    }
}

fun popupLayoutParamType(): Int {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_PHONE else WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
}