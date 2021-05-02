package ${pkg}.view

import android.app.Activity
import android.view.TextureView
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ${pkg}.theme.BlueprintMasterTheme
import ${pkg}.widget.InsetAwareTopAppBar

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalGetImage
@Composable
fun ExoPlayerScreen(
        accessId: String,
        secretId: String,
        controller: NavHostController,
        model: ExoPlayerScreenModel,
) {

    val scaffoldState = rememberScaffoldState()

    var title = "Jetpack Compose"

    var dataState = model.state.collectAsState()

    var context = LocalContext.current as Activity

    var player = remember {
        SimpleExoPlayer.Builder(context).build()
    }

    DisposableEffect(key1 = context) {
        var mediaItem = MediaItem.fromUri("http://192.168.1.20/mp4/1.mp4")
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
        var dataSourceFactory = ProgressiveMediaSource.Factory(httpDataSourceFactory)
        var mediaSource = dataSourceFactory.createMediaSource(mediaItem)
        player.setMediaSource(mediaSource)
        player.playWhenReady = true
        player.prepare()
        onDispose {
            player.release()
        }
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
                AndroidView(
                        modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .align(alignment = Alignment.TopCenter),
                        factory = { context ->
                            TextureView(context)
                        },
                        update = { view ->
                            player.setVideoTextureView(view)
                        }
                )
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
                            player.play()
                        }) {
                            Text(text = "Play")
                        }
                    }
                }
            }
        }
    }

}
