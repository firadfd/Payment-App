package fd.firad.paymentapp.auth.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarPosition
import com.stevdzasan.messagebar.rememberMessageBarState
import fd.firad.paymentapp.auth.data.model.UserSignUpBody
import fd.firad.paymentapp.auth.presentation.viewmodel.AuthViewModel
import fd.firad.paymentapp.common.constants.isEmailValid
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.common.presentation.CustomButton
import fd.firad.paymentapp.common.presentation.CustomTextInputField
import fd.firad.paymentapp.common.presentation.PasswordTextInputField
import fd.firad.paymentapp.nav.navigateBack
import fd.firad.paymentapp.ui.theme.blueColor
import fd.firad.paymentapp.ui.theme.textColor
import kotlinx.coroutines.delay

@Composable
fun RegistrationScreen(
    navController: NavHostController, viewModel: AuthViewModel = hiltViewModel()
) {
    val state = rememberScrollState()
    val context = LocalContext.current
    val messageBarState = rememberMessageBarState()
    var name by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var pass by remember { mutableStateOf("") }
    var passError by remember { mutableStateOf<String?>(null) }
    var conPass by remember { mutableStateOf("") }
    var conPassError by remember { mutableStateOf<String?>(null) }
    var phone by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf<String?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current
    var callApi by rememberSaveable {
        mutableStateOf(false)
    }
    var loading by remember { mutableStateOf(false) }

    if (callApi) {
        LaunchedEffect(viewModel) {
            viewModel.signUpState.collect { state ->
                when (state) {
                    is ApiResponseState.Loading -> {
                        loading = true

                    }

                    is ApiResponseState.Error -> {
                        loading = false
                        messageBarState.addError(exception = Exception(state.errorMessage))
                    }

                    is ApiResponseState.Success -> {
                        loading = false
                        if (state.data.status) {
                            messageBarState.addSuccess(state.data.message)
                            delay(3000L)
                            navController.navigateBack()
                        } else {
                            messageBarState.addError(exception = Exception(state.data.message))
                        }
                        callApi = false

                    }
                }
            }
        }
    }


    ContentWithMessageBar(
        messageBarState = messageBarState,
        position = MessageBarPosition.BOTTOM,
        showCopyButton = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Icon(Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(20.dp)
                    .clickable {
                        navController.navigateBack()
                    })

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Enter your Name",
                    textAlign = TextAlign.Start,
                    style = TextStyle(color = textColor),
                    modifier = Modifier.fillMaxWidth(.9f)
                )
                Spacer(modifier = Modifier.height(5.dp))
                CustomTextInputField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = null
                    },
                    modifier = Modifier.fillMaxWidth(.9f),
                    keyboardType = KeyboardType.Text,
                    placeholder = "",
                    errorMessage = nameError
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Email",
                    textAlign = TextAlign.Start,
                    style = TextStyle(color = textColor),
                    modifier = Modifier.fillMaxWidth(.9f)
                )
                Spacer(modifier = Modifier.height(5.dp))
                CustomTextInputField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                    },
                    modifier = Modifier.fillMaxWidth(.9f),
                    keyboardType = KeyboardType.Email,
                    placeholder = "",
                    errorMessage = emailError
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Phone",
                    textAlign = TextAlign.Start,
                    style = TextStyle(color = textColor),
                    modifier = Modifier.fillMaxWidth(.9f)
                )
                Spacer(modifier = Modifier.height(5.dp))
                CustomTextInputField(
                    value = phone,
                    onValueChange = {
                        phone = it
                        phoneError = null
                    },
                    modifier = Modifier.fillMaxWidth(.9f),
                    keyboardType = KeyboardType.Phone,
                    placeholder = "",
                    errorMessage = phoneError
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Password",
                    textAlign = TextAlign.Start,
                    style = TextStyle(color = textColor),
                    modifier = Modifier.fillMaxWidth(.9f)
                )
                Spacer(modifier = Modifier.height(5.dp))
                PasswordTextInputField(
                    value = pass,
                    onValueChange = {
                        pass = it
                        passError = null
                    },
                    hintText = "Enter your password",
                    errorMessage = passError,
                    modifier = Modifier.fillMaxWidth(.9f)
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Confirm Password",
                    textAlign = TextAlign.Start,
                    style = TextStyle(color = textColor),
                    modifier = Modifier.fillMaxWidth(.9f)
                )
                Spacer(modifier = Modifier.height(5.dp))
                PasswordTextInputField(
                    value = conPass,
                    onValueChange = {
                        conPass = it
                        conPassError = null
                    },
                    hintText = "Enter your confirm password",
                    errorMessage = conPassError,
                    modifier = Modifier.fillMaxWidth(.9f)
                )
                Spacer(modifier = Modifier.height(10.dp))
                TermsAndPrivacyText(onTermsClick = {

                }, onPrivacyClick = {

                })
                Spacer(modifier = Modifier.height(10.dp))
                if (!loading) {
                    CustomButton(
                        text = "Next",
                        color = blueColor,
                        textColor = Color.White,
                        modifier = Modifier.fillMaxWidth(.9f),
                        cornerRadius = 5.dp
                    ) {
                        keyboardController?.hide()
                        if (name.isNotEmpty() && name.length >= 6) {
                            if (email.isNotEmpty() && isEmailValid(email)) {
                                if (phone.isNotEmpty() && phone.length == 11) {
                                    if (pass.isNotEmpty() && pass.length >= 6) {
                                        if (conPass.isNotEmpty() && conPass.length >= 6) {
                                            if (pass == conPass) {
                                                callApi = true
                                                viewModel.userSignUp(
                                                    UserSignUpBody(
                                                        name = name,
                                                        email = email,
                                                        phone = phone,
                                                        password = pass
                                                    )
                                                )
                                            } else {
                                                conPassError = "password not match"
                                            }
                                        } else {
                                            conPassError = "Please enter a valid confirm password"
                                        }
                                    } else {
                                        passError = "Please enter a valid password"
                                    }
                                } else {
                                    phoneError = "Please enter a valid phone number"
                                }
                            } else {
                                emailError = "Please enter a valid email"
                            }
                        } else {
                            nameError = "Please enter your full name"
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Already have an account, Login",
                    color = blueColor,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(.9f)
                        .clickable {
                            navController.navigateBack()
                        })
            }


        }
    }
}


@Preview
@Composable
private fun PreRegistration() {
    RegistrationScreen(navController = rememberNavController())
}

@Composable
fun ApiResponseDialog(
    showDialog: Boolean, message: String, isError: Boolean, onCloseDialog: () -> Unit
) {
    if (showDialog) {
        AlertDialog(onDismissRequest = {

        }, title = { }, text = { Text(message) }, confirmButton = {
            Button(
                onClick = onCloseDialog, modifier = Modifier.padding(8.dp)
            ) {
                Text(if (isError) "Dismiss" else "Login")
            }
        })
    }
}


@Composable
fun TermsAndPrivacyText(onTermsClick: () -> Unit, onPrivacyClick: () -> Unit) {
    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = Color.White,
            )
        ) {
            append("by creating an account, I agree to Trading's ")
        }

        pushStringAnnotation(
            tag = "TERMS", annotation = "terms"
        )
        withStyle(
            style = SpanStyle(
                color = blueColor,
            )
        ) {
            append("Terms of Service")
        }
        pop()

        withStyle(
            style = SpanStyle(
                color = Color.White,
            )
        ) {
            append(" and ")
        }

        pushStringAnnotation(
            tag = "PRIVACY", annotation = "privacy"
        )
        withStyle(
            style = SpanStyle(
                color = blueColor,
            )
        ) {
            append("Privacy Policy.")
        }
        pop()
    }

    ClickableText(
        text = annotatedString, modifier = Modifier.fillMaxWidth(.9f), onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                .firstOrNull()?.let {
                    onTermsClick()
                }
            annotatedString.getStringAnnotations(tag = "PRIVACY", start = offset, end = offset)
                .firstOrNull()?.let {
                    onPrivacyClick()
                }
        }, style = TextStyle(fontSize = 12.sp)
    )
}


