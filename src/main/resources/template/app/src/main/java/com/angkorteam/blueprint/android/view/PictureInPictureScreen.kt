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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.navigationBarsWithImePadding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ${pkg}.R
import ${pkg}.common.P2PFeaturePermission
import ${pkg}.theme.BlueprintMasterTheme
import ${pkg}.widget.InsetAwareTopAppBar

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
        P2PFeaturePermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            model.updateState(state = PictureInPictureScreenModel.DataState.Normal)
        } else {
            model.updateState(state = PictureInPictureScreenModel.DataState.Permission)
        }
    }

    if (!P2PFeaturePermission.hasPictureInPicture(context = context)) {
        model.updateState(PictureInPictureScreenModel.DataState.Permission)
    }

    if (dataState.value is PictureInPictureScreenModel.DataState.Normal) {

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
        }
    } else if (dataState.value is PictureInPictureScreenModel.DataState.Permission) {

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
                                Text(text = "Picture In Picture Setting")
                            }
                        }
                    }
                }
            }
        }
    } else if (dataState.value is PictureInPictureScreenModel.DataState.P2P) {
        Image(
            painter = painterResource(id = R.drawable.picture_in_picture),
            contentDescription = ""
        )
    }

}
