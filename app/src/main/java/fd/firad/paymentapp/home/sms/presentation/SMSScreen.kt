package fd.firad.paymentapp.home.sms.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarPosition
import com.stevdzasan.messagebar.rememberMessageBarState
import fd.firad.paymentapp.common.constants.Constants.sendSMS
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.common.presentation.CustomTopBar
import fd.firad.paymentapp.common.presentation.LoadingScreen
import fd.firad.paymentapp.history.presentation.SMSItem
import fd.firad.paymentapp.home.sms.data.model.AllSMSData
import fd.firad.paymentapp.home.sms.data.model.UpdateStatusBody
import fd.firad.paymentapp.home.sms.presentation.viewmodel.SMSViewModel


@Composable
fun SMSScreen(navController: NavHostController, viewModel: SMSViewModel = hiltViewModel()) {
    val messageBarState = rememberMessageBarState()
    val smsList = remember {
        mutableStateListOf<AllSMSData>()
    }
    val noSMS = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    var dataLoaded by rememberSaveable { mutableStateOf(false) }

    if (viewModel.getApiToken() != null && viewModel.getSecretKey() != null) {
        LaunchedEffect(viewModel) {
            viewModel.pendingSMS(
                apiKey = viewModel.getApiToken()!!, secretKey = viewModel.getSecretKey()!!
            )
        }
    } else {
        messageBarState.addError(exception = Exception("Please Purchase Subscription"))
    }

    LaunchedEffect(viewModel) {
        viewModel.pendingSMSState.collect { state ->
            when (state) {
                is ApiResponseState.Loading -> {
                    loading = true
                }

                is ApiResponseState.Error -> {
                    loading = false
                    Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is ApiResponseState.Success -> {
                    loading = false
                    dataLoaded = true
                    if (state.data.status == "success") {
                        if (state.data.data.isNotEmpty()) {
                            val filteredSmsList =
                                state.data.data.distinctBy { it.id }.filter { it.status != "sent" }
                            smsList.addAll(filteredSmsList)
                        } else {
                            noSMS.value = true
                        }
                    }
                }
            }
        }
    }



    ContentWithMessageBar(
        messageBarState = messageBarState,
        position = MessageBarPosition.TOP,
        showCopyButton = false,
        visibilityDuration = 5000L,
        errorMaxLines = 2
    ) {
        if (loading && !dataLoaded) {
            LoadingScreen()
        } else {

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {
                CustomTopBar(title = "Pending SMS") {
                    navController.navigate("ScreenHome") {
                        popUpTo("ScreenHome") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                if (noSMS.value) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No Pending SMS Found", style = TextStyle(
                                color = Color.Black, fontSize = 18.sp
                            )
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(10.dp)
                    ) {
                        items(smsList) { sms ->
                            SMSItem(
                                smsModelItem = sms, onClick = {
                                    val sendResult =
                                        sendSMS(context, sms.id, sms.number, sms.message)
                                    when (sendResult) {
                                        is SendSmsResult.Success -> {
                                            viewModel.updateSMSStatus(
                                                id = sms.id,
                                                apiKey = viewModel.getApiToken()!!,
                                                secretKey = viewModel.getSecretKey()!!,
                                                request = UpdateStatusBody(status = "sent")
                                            )
                                            smsList.remove(sms)
                                            Toast.makeText(
                                                context, sendResult.message, Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        is SendSmsResult.Failure -> {
                                            Toast.makeText(
                                                context,
                                                sendResult.error.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }

            }
        }
    }
}


sealed class SendSmsResult {
    data class Success(val message: String) : SendSmsResult()
    data class Failure(val error: Throwable) : SendSmsResult()
}
