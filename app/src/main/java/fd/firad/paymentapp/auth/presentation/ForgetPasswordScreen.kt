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
import fd.firad.paymentapp.auth.data.model.ForgotPasswordBody
import fd.firad.paymentapp.auth.presentation.viewmodel.AuthViewModel
import fd.firad.paymentapp.common.constants.isEmailValid
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.common.presentation.CustomButton
import fd.firad.paymentapp.common.presentation.CustomTextInputField
import fd.firad.paymentapp.nav.ScreenForgetPasswordOTP
import fd.firad.paymentapp.ui.theme.blueColor
import fd.firad.paymentapp.ui.theme.grayColor

@Composable
fun ForgetPasswordScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    val state = rememberScrollState()
    val messageBarState = rememberMessageBarState()
    var callApi by rememberSaveable {
        mutableStateOf(false)
    }
    var loading by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    if (callApi) {
        LaunchedEffect(viewModel) {
            viewModel.forgotPasswordState.collect { state ->
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
                            navController.navigate(ScreenForgetPasswordOTP(email = email))
                        } else {
                            messageBarState.addError(exception = Exception(state.data.errors))
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
                text = "Email",
                textAlign = TextAlign.Start,
                style = TextStyle(color = grayColor),
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
                keyboardType = KeyboardType.Text,
                placeholder = "Enter your email",
                errorMessage = emailError
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
                    if ((email.isNotEmpty() && isEmailValid(email)) || (email.isNotEmpty() && email.length in 6..11)) {
                        callApi = true
                        viewModel.forgotPassword(ForgotPasswordBody(login = email))
                    } else {
                        emailError = "Please enter a valid email"
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}