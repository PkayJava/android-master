package ${pkg}.view

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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
                                    Icons.Filled.Login,
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
                                    Icons.Filled.QrCodeScanner,
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
                Divider()
                ListItem(
                        icon = {
                            Icon(
                                    Icons.Filled.ManageSearch,
                                    contentDescription = null,
                                    modifier = Modifier.size(56.dp)
                            )
                        },
                        overlineText = { Text(text = "v1.0") },
                        text = { Text("Text / OCR / Luhn") },
                        secondaryText = { Text(text = "Prototype") },
                        trailing = { Text(text = "By PkayJava") },
                        modifier = Modifier.clickable {
                            val route = "/luhn/${accessId}/${secretId}"
                            controller.navigate(route = route)
                        }
                )
                Divider()
                ListItem(
                        icon = {
                            Icon(
                                    Icons.Filled.CameraAlt,
                                    contentDescription = null,
                                    modifier = Modifier.size(56.dp)
                            )
                        },
                        overlineText = { Text(text = "v1.0") },
                        text = { Text("Take Picture") },
                        secondaryText = { Text(text = "Prototype") },
                        trailing = { Text(text = "By PkayJava") },
                        modifier = Modifier.clickable {
                            val route = "/camera/${accessId}/${secretId}"
                            controller.navigate(route = route)
                        }
                )
                Divider()
                ListItem(
                        icon = {
                            Icon(
                                    Icons.Filled.FitScreen,
                                    contentDescription = null,
                                    modifier = Modifier.size(56.dp)
                            )
                        },
                        overlineText = { Text(text = "v1.0") },
                        text = { Text("Picture In Picture") },
                        secondaryText = { Text(text = "Prototype") },
                        trailing = { Text(text = "By PkayJava") },
                        modifier = Modifier.clickable {
                            val route = "/p2p/${accessId}/${secretId}"
                            controller.navigate(route = route)
                        }
                )
                Divider()
                ListItem(
                        icon = {
                            Icon(
                                    Icons.Filled.SmartScreen,
                                    contentDescription = null,
                                    modifier = Modifier.size(56.dp)
                            )
                        },
                        overlineText = { Text(text = "v1.0") },
                        text = { Text("Overlay Window") },
                        secondaryText = { Text(text = "Prototype") },
                        trailing = { Text(text = "By PkayJava") },
                        modifier = Modifier.clickable {
                            val route = "/overlay/${accessId}/${secretId}"
                            controller.navigate(route = route)
                        }
                )
            }
        }
    }

}