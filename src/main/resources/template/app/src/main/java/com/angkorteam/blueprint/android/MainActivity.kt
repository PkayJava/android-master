package ${pkg}

import android.app.AppOpsManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
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
                        val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
//                        val model: PictureInPictureScreenModel =
//                            viewModel("PictureInPictureScreenModel", factory)
                        val accessId = navBackStackEntry.arguments?.getString("accessId")!!
                        val secretId = navBackStackEntry.arguments?.getString("secretId")!!
                        PictureInPictureScreen(
                                accessId = accessId,
                                secretId = secretId,
                                controller = controller,
                                model = p2pModel
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

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val appOpsManager = this.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            var mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appOpsManager.unsafeCheckOpRaw(
                        AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
                        android.os.Process.myUid(),
                        this.packageName
                )
            } else {
                appOpsManager.checkOpNoThrow(
                        AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
                        android.os.Process.myUid(),
                        this.packageName
                )
            }
            if (mode != AppOpsManager.MODE_ALLOWED) {
                p2pModel.updateState(PictureInPictureScreenModel.DataState.Permission)
            } else {
                p2pModel.updateState(PictureInPictureScreenModel.DataState.Normal)
            }
        }
    }

}