package ${pkg}.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import ${pkg}.R
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun LoginScreen(
        controller: NavHostController,
        model: LoginScreenModel,
        hasIme: Boolean,
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

    var scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState,
            snackbarHost = {
                SnackbarHost(
                        hostState = scaffoldState.snackbarHostState,
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

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            var (imageRef, loginUIRef, usernameRef, passwordRef, loginRef) = createRefs()

            Image(
                    painter = image, contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                            .constrainAs(imageRef) {
                                top.linkTo(parent.top, 0.dp)
                                bottom.linkTo(loginUIRef.top, 0.dp)
                                height = Dimension.fillToConstraints
                                centerHorizontallyTo(loginUIRef)
                            }
                            .aspectRatio(1f)
            )

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
                    },
                    modifier = Modifier.constrainAs(loginUIRef) {
                        if (hasIme) {
                            bottom.linkTo(usernameRef.top, 4.dp)
                        } else {
                            bottom.linkTo(usernameRef.top, 8.dp)
                        }
                        start.linkTo(parent.start, 16.dp)
                        end.linkTo(parent.end, 16.dp)
                        width = Dimension.fillToConstraints
                    },
                    textAlign = TextAlign.Center
            )

            OutlinedTextField(
                    value = loginValue,
                    onValueChange = { loginValue = it },
                    label = { Text(text = "Username") },
                    placeholder = { Text(text = "Username") },
                    singleLine = true,
                    modifier = Modifier
                            .constrainAs(usernameRef) {
                                bottom.linkTo(passwordRef.top)
                                start.linkTo(parent.start, 16.dp)
                                end.linkTo(parent.end, 16.dp)
                                width = Dimension.fillToConstraints
                            },
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
                            .focusRequester(focusRequester = focusRequester)
                            .constrainAs(passwordRef) {
                                if (hasIme) {
                                    bottom.linkTo(loginRef.top, 4.dp)
                                } else {
                                    bottom.linkTo(loginRef.top, 36.dp)
                                }
                                start.linkTo(parent.start, 16.dp)
                                end.linkTo(parent.end, 16.dp)
                                width = Dimension.fillToConstraints
                            },
                    keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            autoCorrect = false,
                            imeAction = ImeAction.None
                    ),
            )

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
                    modifier = Modifier.constrainAs(loginRef) {
                        if (hasIme) {
                            bottom.linkTo(parent.bottom, 8.dp)
                        } else {
                            bottom.linkTo(parent.bottom, 36.dp)
                        }
                        start.linkTo(parent.start, 16.dp)
                        end.linkTo(parent.end, 16.dp)
                        height = Dimension.value(50.dp)
                        width = Dimension.fillToConstraints
                    }
            ) {
                Text(text = "Login", fontSize = 20.sp)
            }

        }
    }
}