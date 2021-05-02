package ${pkg}

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ${pkg}.view.*

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalGetImage
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val p2pModel: PictureInPictureScreenModel by viewModels()

    @ExperimentalAnimatedInsets
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {

            ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
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
                        LoginScreen(controller = controller, model = model)
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
                            route = "/camera/{accessId}/{secretId}"
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