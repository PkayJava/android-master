package ${pkg}

import android.os.Bundle
import androidx.activity.compose.setContent
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
                }
            }
        }
    }

}