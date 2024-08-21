package fd.firad.paymentapp.home.sms.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.common.presentation.CustomTopBar
import fd.firad.paymentapp.home.sms.data.model.PaymentSendSmsBody
import fd.firad.paymentapp.home.sms.presentation.viewmodel.SMSViewModel
import fd.firad.paymentapp.room.entity.SmsEntity


@Composable
fun FailedSMSScreen(navController: NavHostController, viewModel: SMSViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var sendSMS by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.loadFailedSms()
    }
    val failedSmsList by viewModel.failedSmsList.collectAsState(emptyList())
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        CustomTopBar(title = "Failed Payment SMS") {
            navController.navigate("ScreenHome") {
                popUpTo("ScreenHome") {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        if (failedSmsList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No Failed Transaction SMS Found", style = TextStyle(
                        color = Color.Black, fontSize = 18.sp
                    )
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(10.dp)
            ) {
                items(failedSmsList) { sms ->
                    TraSMSItem(
                        smsModelItem = sms,
                        onDeleteClick = {
                            viewModel.deleteSms(sms)
                        },
                        viewModel = viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TraSMSItem(
    smsModelItem: SmsEntity,
    viewModel: SMSViewModel,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    var currentSmsId by remember { mutableStateOf<Long?>(null) }
    LaunchedEffect(viewModel.paymentSMSState) {
        viewModel.paymentSMSState.collect { state ->
            when (state) {
                is ApiResponseState.Loading -> {

                }

                is ApiResponseState.Error -> {

                }

                is ApiResponseState.Success -> {
                    if (state.data.status) {
                        currentSmsId?.let { id ->
                            viewModel.deleteSms(
                                SmsEntity(
                                    id.toInt(), smsModelItem.sender, smsModelItem.msg
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    Row(
        modifier = modifier
            .border(
                width = 1.dp, color = Color.White, shape = RoundedCornerShape(10.dp)
            )
            .background(Color.Black, shape = RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center, modifier = Modifier.weight(.9f)
        ) {
            Text(
                text = smsModelItem.sender, style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = smsModelItem.msg, style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                ), maxLines = 4, overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { onDeleteClick() }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }

            IconButton(onClick = {
                currentSmsId = smsModelItem.id?.toLong()
                viewModel.paymentSms(
                    apiKey = viewModel.getApiToken()!!,
                    secretKey = viewModel.getSecretKey()!!,
                    PaymentSendSmsBody(smsModelItem.sender, smsModelItem.msg)
                )
            }) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Resend",
                    tint = Color.Green
                )
            }
        }
    }
}


