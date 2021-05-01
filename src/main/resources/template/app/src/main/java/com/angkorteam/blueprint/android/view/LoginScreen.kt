package ${pkg}.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ${pkg}.R

@ExperimentalAnimatedInsets
@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun LoginScreen(
    controller: NavHostController,
    model: LoginScreenModel,
) {

    val image = painterResource(id = R.drawable.login_image)

    var loginValue by remember {
        model.loginValue
    }
    var passwordValue by remember {
        model.passwordValue
    }

    var dataState = model.state.collectAsState()

    val passwordVisibility = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val insets = LocalWindowInsets.current

    var scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = scaffoldState.snackbarHostState,
                modifier = Modifier.navigationBarsWithImePadding()
            )
        }) {

        if (dataState.value is LoginScreenModel.DataState.Error) {
            LaunchedEffect(key1 = scaffoldState.snackbarHostState) {
                scaffoldState.snackbarHostState.showSnackbar(message = "Access Denied")
            }
        }
        if (dataState.value is LoginScreenModel.DataState.Success) {
            SideEffect {
                var dto = dataState.value as LoginScreenModel.DataState.Success
                val route = "/menu/${dto.data.accessId}/${dto.data.secretId}"
                controller.navigate(route = route)
                model.reset()
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsWithImePadding(),
            color = Color.Transparent,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = image, contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .fillMaxWidth(if (insets.ime.isVisible) 0.35f else 0.7f)
                        .aspectRatio(1f)
                )
                Column(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp,
                                    fontSize = 26.sp
                                )
                            ) {
                                append(text = "Login ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Normal,
                                    letterSpacing = 2.sp,
                                    fontSize = 26.sp
                                )
                            ) {
                                append(text = "UI")
                            }
                        }
                    )
                    Spacer(modifier = Modifier.padding(if (insets.ime.isVisible) 4.dp else 8.dp))
                    OutlinedTextField(
                        value = loginValue,
                        onValueChange = { it -> loginValue = it },
                        label = { Text(text = "Username") },
                        placeholder = { Text(text = "Username") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            autoCorrect = false,
                            imeAction = ImeAction.None
                        ),
                    )
                    OutlinedTextField(
                        value = passwordValue,
                        onValueChange = { passwordValue = it },
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisibility.value = !passwordVisibility.value
                            }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.password_eye),
                                    tint = if (passwordVisibility.value) Color(0xFF7048B6) else Color.Gray,
                                    contentDescription = "",
                                )
                            }
                        },
                        label = { Text("Password") },
                        placeholder = { Text(text = "Password") },
                        singleLine = true,
                        visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .focusRequester(focusRequester = focusRequester),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            autoCorrect = false,
                            imeAction = ImeAction.None
                        ),
                    )
                    Spacer(modifier = Modifier.padding(if (insets.ime.isVisible) 4.dp else 16.dp))
                    Button(
                        onClick = {
                            if (dataState.value is LoginScreenModel.DataState.Loading) {
                            } else {
                                model.login(
                                    login = loginValue,
                                    password = passwordValue,
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp)
                    ) {
                        Text(text = "Login", fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.padding(if (insets.ime.isVisible) 8.dp else 36.dp))
                }
            }
        }
    }
}