package ${pkg}

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ${pkg}.effect.LifecycleEffect
import ${pkg}.effect.SystemInsetsEffect
import ${pkg}.service.ComposableService
import ${pkg}.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalGetImage
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val p2pModel: PictureInPictureScreenModel by viewModels()

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(
                window, false
        )

        setContent {

            var context = LocalContext.current

            var systemPadding by remember {
                mutableStateOf(PaddingValues(start = 0.dp, top = 24.dp, end = 0.dp, bottom = 48.dp))
            }

            var hasStatusBar by remember {
                mutableStateOf(true)
            }

            var hasIme by remember {
                mutableStateOf(false)
            }

            var hasNavigationBar by remember {
                mutableStateOf(true)
            }

            LifecycleEffect { _, lifecycle ->
                when (lifecycle) {
                    Lifecycle.Event.ON_RESUME -> {
                        var intent =
                                Intent(context, ComposableService::class.java)
                        intent.putExtra(ComposableService.NAME, "OverlayService")
                        intent.putExtra("APP", "Foreground")
                        context.startService(intent)
                    }
                    Lifecycle.Event.ON_PAUSE -> {
                        var intent =
                                Intent(context, ComposableService::class.java)
                        intent.putExtra(ComposableService.NAME, "OverlayService")
                        intent.putExtra("APP", "Background")
                        context.startService(intent)
                    }
                }
            }

            SystemInsetsEffect { padding, statusBar, ime, navigationBar ->
                systemPadding = padding
                hasStatusBar = statusBar
                hasNavigationBar = navigationBar
                hasIme = ime
            }

            Surface(
                    modifier = Modifier
                            .fillMaxSize()
                            .padding(systemPadding)
            ) {
                val controller = rememberNavController()
                NavHost(
                        navController = controller,
                        startDestination = "/login"
                ) {
                    composable(
                            route = "/login"
                    ) { navBackStackEntry ->
                        val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                        val model: LoginScreenModel = viewModel("LoginScreenModel", factory)
                        LoginScreen(controller = controller, model = model, hasIme = hasIme)
                    }
                    composable(
                            route = "/menu/{accessId}/{secretId}"
                    ) { navBackStackEntry ->
                        val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                        val model: MenuScreenModel = viewModel("MenuScreenModel", factory)
                        val accessId = navBackStackEntry.arguments?.getString("accessId")!!
                        val secretId = navBackStackEntry.arguments?.getString("secretId")!!
                        MenuScreen(
                                accessId = accessId,
                                secretId = secretId,
                                controller = controller,
                                model = model
                        )
                    }
                    composable(
                            route = "/barcode/{accessId}/{secretId}"
                    ) { navBackStackEntry ->
                        val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                        val model: BarcodeScreenModel = viewModel("BarcodeScreenModel", factory)
                        val accessId = navBackStackEntry.arguments?.getString("accessId")!!
                        val secretId = navBackStackEntry.arguments?.getString("secretId")!!
                        BarcodeScreen(
                                accessId = accessId,
                                secretId = secretId,
                                controller = controller,
                                model = model
                        )
                    }
                    composable(
                            route = "/luhn/{accessId}/{secretId}"
                    ) { navBackStackEntry ->
                        val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                        val model: ImageOCRScreenModel = viewModel("ImageOCRScreenModel", factory)
                        val accessId = navBackStackEntry.arguments?.getString("accessId")!!
                        val secretId = navBackStackEntry.arguments?.getString("secretId")!!
                        ImageOCRScreen(
                                accessId = accessId,
                                secretId = secretId,
                                controller = controller,
                                model = model
                        )
                    }
                    composable(
                            route = "/picture/{accessId}/{secretId}"
                    ) { navBackStackEntry ->
                        val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                        val model: TakePictureScreenModel =
                                viewModel("TakePictureScreenModel", factory)
                        val accessId = navBackStackEntry.arguments?.getString("accessId")!!
                        val secretId = navBackStackEntry.arguments?.getString("secretId")!!
                        TakePictureScreen(
                                accessId = accessId,
                                secretId = secretId,
                                controller = controller,
                                model = model
                        )
                    }
                    composable(
                            route = "/p2p/{accessId}/{secretId}"
                    ) { navBackStackEntry ->
                        val accessId = navBackStackEntry.arguments?.getString("accessId")!!
                        val secretId = navBackStackEntry.arguments?.getString("secretId")!!
                        PictureInPictureScreen(
                                accessId = accessId,
                                secretId = secretId,
                                controller = controller,
                                model = p2pModel
                        )
                    }
                    composable(
                            route = "/overlay/{accessId}/{secretId}"
                    ) { navBackStackEntry ->
                        val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                        val model: OverlayWindowScreenModel =
                                viewModel("OverlayWindowScreenModel", factory)
                        val accessId = navBackStackEntry.arguments?.getString("accessId")!!
                        val secretId = navBackStackEntry.arguments?.getString("secretId")!!
                        OverlayWindowScreen(
                                accessId = accessId,
                                secretId = secretId,
                                controller = controller,
                                model = model
                        )
                    }
                    composable(
                            route = "/exo/{accessId}/{secretId}"
                    ) { navBackStackEntry ->
                        val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                        val model: ExoPlayerScreenModel =
                                viewModel("ExoPlayerScreenModel", factory)
                        val accessId = navBackStackEntry.arguments?.getString("accessId")!!
                        val secretId = navBackStackEntry.arguments?.getString("secretId")!!
                        ExoPlayerScreen(
                                accessId = accessId,
                                secretId = secretId,
                                controller = controller,
                                model = model
                        )
                    }
                }
            }
        }
    }

    override fun onPictureInPictureModeChanged(
            isInPictureInPictureMode: Boolean,
            newConfig: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
            p2pModel.updateState(PictureInPictureScreenModel.DataState.P2P)
        } else {
            p2pModel.updateState(PictureInPictureScreenModel.DataState.Normal)
        }
    }

    companion object {
        const val DEBUG = MainApplication.DEBUG
        const val TAG = "MainActivity"
    }

}