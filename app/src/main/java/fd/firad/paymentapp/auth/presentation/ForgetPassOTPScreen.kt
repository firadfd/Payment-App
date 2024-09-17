package fd.firad.paymentapp.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarPosition
import com.stevdzasan.messagebar.rememberMessageBarState
import fd.firad.paymentapp.auth.data.model.VerifyOTPBody
import fd.firad.paymentapp.auth.presentation.viewmodel.AuthViewModel
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.common.presentation.CustomButton
import fd.firad.paymentapp.common.presentation.CustomTextInputField
import fd.firad.paymentapp.nav.ScreenForgetPasswordOTP
import fd.firad.paymentapp.nav.ScreenLogin
import fd.firad.paymentapp.ui.theme.blueColor
import fd.firad.paymentapp.ui.theme.grayColor

@Composable
fun ForgetPasswordOTPScreen(
    navController: NavHostController,
    email: String,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    var otp by remember { mutableStateOf("") }
    var otpError by remember { mutableStateOf<String?>(null) }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var conPassword by remember { mutableStateOf("") }
    var conPasswordError by remember { mutableStateOf<String?>(null) }
    val state = rememberScrollState()
    val messageBarState = rememberMessageBarState()
    var callApi by rememberSaveable {
        mutableStateOf(false)
    }
    var loading by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    if (callApi) {
        LaunchedEffect(viewModel) {
            viewModel.verifyOTPState.collect { state ->
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
                            navController.navigate(ScreenLogin) {
                                popUpTo(0) { inclusive = true }  // This clears the entire backstack
                                launchSingleTop = true           // Ensures only one instance of the Home screen
                            }
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
        showCopyButton = false,
        errorMaxLines = 2
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
                .verticalScroll(state),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "OTP",
                textAlign = TextAlign.Start,
                style = TextStyle(color = grayColor),
                modifier = Modifier.fillMaxWidth(.9f)
            )
            Spacer(modifier = Modifier.height(5.dp))
            CustomTextInputField(
                value = otp,
                onValueChange = {
                    otp = it
                    otpError = null
                },
                modifier = Modifier.fillMaxWidth(.9f),
                keyboardType = KeyboardType.Text,
                placeholder = "Enter your otp",
                errorMessage = otpError
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "New Password",
                textAlign = TextAlign.Start,
                style = TextStyle(color = grayColor),
                modifier = Modifier.fillMaxWidth(.9f)
            )
            Spacer(modifier = Modifier.height(5.dp))
            CustomTextInputField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                modifier = Modifier.fillMaxWidth(.9f),
                keyboardType = KeyboardType.Text,
                placeholder = "Enter your new password",
                errorMessage = passwordError
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Confirm Password",
                textAlign = TextAlign.Start,
                style = TextStyle(color = grayColor),
                modifier = Modifier.fillMaxWidth(.9f)
            )
            Spacer(modifier = Modifier.height(5.dp))
            CustomTextInputField(
                value = conPassword,
                onValueChange = {
                    conPassword = it
                    conPasswordError = null
                },
                modifier = Modifier.fillMaxWidth(.9f),
                keyboardType = KeyboardType.Text,
                placeholder = "Enter your confirm password",
                errorMessage = conPasswordError
            )
            Spacer(modifier = Modifier.height(25.dp))
            if (!loading) {
                CustomButton(
                    text = "Send OTP",
                    textColor = Color.White,
                    color = blueColor,
                    modifier = Modifier.fillMaxWidth(.9f),
                    cornerRadius = 5.dp
                ) {
                    keyboardController?.hide()
                    if (otp.isNotEmpty() && otp.length in 6..11) {
                        if (password.isNotEmpty() && password.length in 6..11) {
                            if (conPassword.isNotEmpty() && conPassword.length in 6..11) {
                                if (password == conPassword) {
                                    callApi = true
                                    viewModel.verifyOtp(
                                        VerifyOTPBody(
                                            login = email,
                                            otp = otp,
                                            password = password
                                        )
                                    )
                                } else {
                                    conPasswordError = "Password does not match"
                                }
                            } else {
                                conPasswordError = "Please enter a valid Confirm Password"
                            }
                        } else {
                            conPasswordError = "Please enter a valid New Password"

                        }
                    } else {
                        otpError = "Please enter a valid OTP"
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

    }
}