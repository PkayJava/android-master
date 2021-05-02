package ${pkg}.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.navigationBarsWithImePadding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ${pkg}.R
import ${pkg}.common.LifecycleEffect
import ${pkg}.common.LocalService
import ${pkg}.common.OverlayWindowPermission
import ${pkg}.theme.BlueprintMasterTheme
import ${pkg}.widget.InsetAwareTopAppBar

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

    if (dataState.value is OverlayWindowScreenModel.DataState.SHOW ||
            dataState.value is OverlayWindowScreenModel.DataState.HIDE
    ) {

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
                                        var intent = Intent(context, LocalService::class.java)
                                        intent.putExtra("ACTION", "SHOW")
                                        context.startService(intent)
                                    }
                                }) {
                                    Text(text = "Show")
                                }
                            } else if (dataState.value is OverlayWindowScreenModel.DataState.SHOW) {
                                Button(onClick = {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        var intent = Intent(context, LocalService::class.java)
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
        }
    } else if (dataState.value is OverlayWindowScreenModel.DataState.Permission) {

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