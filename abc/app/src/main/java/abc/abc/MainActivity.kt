package abc.abc

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import abc.abc.view.DetailScreen
import abc.abc.view.DetailScreenModel
import abc.abc.view.FontScreen
import abc.abc.view.ListScreen
import abc.abc.view.ListScreenModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val controller = rememberNavController()
            NavHost(
                navController = controller,
                startDestination = "/home"
            ) {
                composable(
                    route = "/home"
                ) { navBackStackEntry ->
                    FontScreen()
                }
                composable(
                    route = "/list"
                ) { navBackStackEntry ->
                    val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                    val model: ListScreenModel = viewModel("ListScreenModel", factory)
                    ListScreen(controller = controller, model = model)
                }
                composable(
                    route = "/detail/{entityId}",
                    arguments = listOf(
                        navArgument("entityId") {
                            type = NavType.IntType
                        }
                    )
                ) { navBackStackEntry ->
                    val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                    val model: DetailScreenModel = viewModel("DetailScreenModel", factory)
                    DetailScreen(
                        entityId = navBackStackEntry.arguments?.getInt("entityId"),
                        model = model,
                    )
                }
            }
        }
    }

}