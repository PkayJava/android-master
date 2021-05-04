package ${pkg}.view


import android.app.Activity
import android.app.PictureInPictureParams
import android.os.Build
import android.util.Rational
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
import ${pkg}.R
import ${pkg}.effect.LifecycleEffect
import ${pkg}.permission.PictureInPicturePermission
import ${pkg}.theme.BlueprintMasterTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalGetImage
@Composable
fun PictureInPictureScreen(
        accessId: String,
        secretId: String,
        controller: NavHostController,
        model: PictureInPictureScreenModel,
) {

    val scaffoldState = rememberScaffoldState()

    var title = "Jetpack Compose"

    var dataState = model.state.collectAsState()

    var context = LocalContext.current as Activity

    val launcher = rememberLauncherForActivityResult(
            PictureInPicturePermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            model.updateState(state = PictureInPictureScreenModel.DataState.Normal)
        } else {
            model.updateState(state = PictureInPictureScreenModel.DataState.Permission)
        }
    }

    LifecycleEffect { owner, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            if (!PictureInPicturePermission.hasPictureInPicture(context = context)) {
                model.updateState(PictureInPictureScreenModel.DataState.Permission)
            }
        }
    }

    SideEffect {
        if (!PictureInPicturePermission.hasPictureInPicture(context = context)) {
            model.updateState(PictureInPictureScreenModel.DataState.Permission)
        } else {
            if (dataState.value == PictureInPictureScreenModel.DataState.Permission) {
                model.updateState(PictureInPictureScreenModel.DataState.Normal)
            } else if (dataState.value == PictureInPictureScreenModel.DataState.Normal) {
                model.updateState(PictureInPictureScreenModel.DataState.Normal)
            } else if (dataState.value == PictureInPictureScreenModel.DataState.P2P) {
                model.updateState(PictureInPictureScreenModel.DataState.P2P)
            }
        }
    }

    when (dataState.value) {
        is PictureInPictureScreenModel.DataState.P2P -> {
            Image(
                    painter = painterResource(id = R.drawable.picture_in_picture),
                    contentDescription = ""
            )
        }
        is PictureInPictureScreenModel.DataState.Normal, PictureInPictureScreenModel.DataState.Permission -> {
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
                        is PictureInPictureScreenModel.DataState.Normal -> {
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
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                val pictureInPictureParamsBuilder =
                                                        PictureInPictureParams.Builder()
                                                pictureInPictureParamsBuilder.setAspectRatio(
                                                        Rational(
                                                                16,
                                                                9
                                                        )
                                                )
//                                    val actions = ArrayList<RemoteAction>()
//                                    actions.add(
//                                        RemoteAction(
//                                            android.graphics.drawable.Icon.createWithResource(
//                                                context,
//                                                R.drawable.ic_launcher_background
//                                            ),
//                                            "Title", "Description",
//                                            PendingIntent.getActivity(
//                                                context, 3,
//                                                Intent(Intent.ACTION_VIEW, Uri.parse("ss")),
//                                                0
//                                            )
//                                        )
//                                    )
//                                    pictureInPictureParamsBuilder.setActions(actions)
                                                context.enterPictureInPictureMode(
                                                        pictureInPictureParamsBuilder.build()
                                                )
                                            }
                                        }) {
                                            Text(text = "Picture In Picture")
                                        }
                                    }
                                }
                            }
                        }
                        is PictureInPictureScreenModel.DataState.Permission -> {
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
    }
}
