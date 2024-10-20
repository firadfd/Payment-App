package fd.firad.paymentapp.home.sms.presentation

import android.util.Log
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarPosition
import com.stevdzasan.messagebar.rememberMessageBarState
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.common.presentation.CustomButton
import fd.firad.paymentapp.common.presentation.CustomTopBar
import fd.firad.paymentapp.common.presentation.PasswordTextInputField
import fd.firad.paymentapp.home.sms.data.model.ChangePasswordBody
import fd.firad.paymentapp.home.sms.presentation.viewmodel.SMSViewModel
import fd.firad.paymentapp.nav.navigateBack
import fd.firad.paymentapp.ui.theme.blueColor

@Composable
fun PassChangeScreen(
    navController: NavHostController,
    viewModel: SMSViewModel = hiltViewModel(),
    isLogout: () -> Unit
) {

    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var newPassword by remember { mutableStateOf("") }
    var newPasswordError by remember { mutableStateOf<String?>(null) }
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
            viewModel.changePassState.collect { state ->
                when (state) {
                    is ApiResponseState.Loading -> {
                        loading = true

                    }

                    is ApiResponseState.Error -> {
                        loading = false
                        messageBarState.addError(exception = Exception(state.errorMessage))
                        Log.e("TAG", "PassChangeScreen: ${state.errorMessage}", )
                    }

                    is ApiResponseState.Success -> {
                        loading = false
                        if (state.data.status) {
                            messageBarState.addSuccess(message = state.data.message)
                            viewModel.deleteToken()
                            isLogout.invoke()
                        } else {
                            Log.e("TAG", "${state.data.message}", )
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
                .background(color = Color.White)
                .verticalScroll(state),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTopBar(title = "Password Change") {
                navController.navigateBack()
            }
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "Enter your Current Password",
                textAlign = TextAlign.Start,
                style = TextStyle(color = Color.Black),
                modifier = Modifier.fillMaxWidth(.9f)
            )
            Spacer(modifier = Modifier.height(5.dp))
            PasswordTextInputField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                backgroundColor = Color.DarkGray,
                hintText = "Enter your Current password",
                errorMessage = passwordError,
                modifier = Modifier.fillMaxWidth(.9f)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Enter your New Password",
                textAlign = TextAlign.Start,
                style = TextStyle(color = Color.Black),
                modifier = Modifier.fillMaxWidth(.9f)
            )
            Spacer(modifier = Modifier.height(5.dp))
            PasswordTextInputField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    newPasswordError = null
                },
                backgroundColor = Color.DarkGray,
                hintText = "Enter your New password",
                errorMessage = newPasswordError,
                modifier = Modifier.fillMaxWidth(.9f)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Enter your Confirm Password",
                textAlign = TextAlign.Start,
                style = TextStyle(color = Color.Black),
                modifier = Modifier.fillMaxWidth(.9f)
            )
            Spacer(modifier = Modifier.height(5.dp))
            PasswordTextInputField(
                value = conPassword,
                onValueChange = {
                    conPassword = it
                    conPasswordError = null
                },
                backgroundColor = Color.DarkGray,
                hintText = "Enter your Confirm password",
                errorMessage = conPasswordError,
                modifier = Modifier.fillMaxWidth(.9f)
            )
            Spacer(modifier = Modifier.height(25.dp))
            if (!loading) {
                CustomButton(
                    text = "Next",
                    textColor = Color.White,
                    color = blueColor,
                    modifier = Modifier.fillMaxWidth(.9f),
                    cornerRadius = 5.dp
                ) {
                    keyboardController?.hide()
                    // Validation logic
                    when {
                        password.isEmpty() -> {
                            passwordError = "Current password cannot be empty"
                        }

                        password.length < 6 -> {
                            passwordError = "Current password must be at least 6 characters"
                        }

                        newPassword.isEmpty() -> {
                            newPasswordError = "New password cannot be empty"
                        }

                        newPassword.length < 6 -> {
                            newPasswordError = "New password must be at least 6 characters"
                        }

                        conPassword.isEmpty() -> {
                            conPasswordError = "Confirm password cannot be empty"
                        }

                        conPassword.length < 6 -> {
                            conPasswordError = "Confirm password must be at least 6 characters"
                        }

                        newPassword != conPassword -> {
                            conPasswordError = "Passwords do not match"
                        }

                        else -> {
                            callApi = true
                            viewModel.userPassChange(
                                ChangePasswordBody(
                                    old_password = password,
                                    new_password = newPassword
                                )
                            )

                        }
                    }


                }
            }

        }
    }
}


@Preview
@Composable
private fun PreLogin() {
    PassChangeScreen(rememberNavController()){

    }

}