package ${pkg}.view

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.google.accompanist.insets.navigationBarsWithImePadding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ${pkg}.theme.BlueprintMasterTheme
import ${pkg}.widget.InsetAwareTopAppBar

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalGetImage
@Composable
fun MenuScreen(
    accessId: String,
    secretId: String,
    controller: NavHostController,
    model: MenuScreenModel,
) {

    val scaffoldState = rememberScaffoldState()

    var title = "Jetpack Compose"

    var dataState = model.state.collectAsState()

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
            Column {
                ListItem(
                    icon = {
                        Icon(
                            Icons.Filled.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp)
                        )
                    },
                    overlineText = { Text(text = "v1.0") },
                    text = { Text("Login") },
                    secondaryText = { Text(text = "Prototype") },
                    trailing = { Text(text = "By PkayJava") },
                    modifier = Modifier.clickable {
                        val route = "/login"
                        controller.navigate(route = route)
                    }
                )
                Divider()
                ListItem(
                    icon = {
                        Icon(
                            Icons.Filled.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp)
                        )
                    },
                    overlineText = { Text(text = "v1.0") },
                    text = { Text("Barcode / QRCode") },
                    secondaryText = { Text(text = "Prototype") },
                    trailing = { Text(text = "By PkayJava") },
                    modifier = Modifier.clickable {
                        val route = "/barcode/${accessId}/${secretId}"
                        controller.navigate(route = route)
                    }
                )
            }
        }
    }

}