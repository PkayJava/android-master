package ${pkg}.view

import android.app.Activity
import android.content.Intent
import android.view.TextureView
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController


import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ${pkg}.effect.ServiceEffect
import ${pkg}.service.ComposableService
import ${pkg}.theme.BlueprintMasterTheme


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

    var player_state by remember {
        mutableStateOf<SimpleExoPlayer?>(null)
    }

    ServiceEffect(
            serviceName = "ExoPlayer",
            serviceClass = ComposableService::class.java,
            onConnected = { registry ->
                if (registry["player"] == null) {
                    var player = SimpleExoPlayer.Builder(context).build()
                    registry["player"] = player
                }
                var player = registry["player"] as SimpleExoPlayer
                player_state = player
            },
            onDisconnected = {registry->
                var player = registry["player"] as SimpleExoPlayer
                player.stop()
            }
    ) { intent, registry ->
        var player = registry["player"] as SimpleExoPlayer
        var command = intent.getStringExtra("command")
        if ("PLAY" == command) {
            val uri = intent.getStringExtra("uri")
            val mediaItem = MediaItem.fromUri(uri!!)
            val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            val dataSourceFactory = ProgressiveMediaSource.Factory(httpDataSourceFactory)
            val mediaSource = dataSourceFactory.createMediaSource(mediaItem)
            player.setMediaSource(mediaSource)
            player.playWhenReady = true
            player.prepare()
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
            Box(
                    modifier = Modifier
                            .fillMaxSize()
            ) {
                if (player_state != null) {
                    AndroidView(
                            modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f)
                                    .align(alignment = Alignment.TopCenter),
                            factory = { context ->
                                TextureView(context)
                            },
                            update = { view ->
                                player_state!!.setVideoTextureView(view)
                            }
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
                            var intent = Intent(context, ComposableService::class.java)
                            intent.putExtra(ComposableService.NAME, "ExoPlayer")
                            intent.putExtra("command", "PLAY")
                            intent.putExtra("uri", "http://192.168.1.20/mp4/1.mp4")
                            context.startService(intent)
                        }) {
                            Text(text = "Play")
                        }
                    }
                }
            }
        }
    }

}
