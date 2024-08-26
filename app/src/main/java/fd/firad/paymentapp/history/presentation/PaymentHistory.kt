package fd.firad.paymentapp.history.presentation


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fd.firad.paymentapp.common.constants.Constants.convertIsoToCustomFormat
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.home.sms.data.model.AllSMSData
import fd.firad.paymentapp.home.sms.presentation.viewmodel.SMSViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@Composable
fun PaymentHistory(modifier: Modifier = Modifier, viewModel: SMSViewModel = hiltViewModel()) {
    val state = rememberLazyListState()
    val context = LocalContext.current

    val smsList = remember {
        mutableStateListOf<AllSMSData>()
    }
    val noSMS = remember {
        mutableStateOf(false)
    }
    var loading by remember { mutableStateOf(false) }
    var dataLoaded by rememberSaveable { mutableStateOf(false) }

    if (viewModel.getApiToken() != null && viewModel.getSecretKey() != null) {
        LaunchedEffect(viewModel) {
            viewModel.allSMS(
                apiKey = viewModel.getApiToken()!!, secretKey = viewModel.getSecretKey()!!
            )
        }
    } else {
        Toast.makeText(context, "Please Purchase Subscription", Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(viewModel) {
        viewModel.allSMSState.collect { state ->
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
                                state.data.data.filter { it.status == "sent" }
                            smsList.addAll(filteredSmsList)
                        } else {
                            noSMS.value = true
                        }
                    }
                }
            }
        }
    }


    LazyColumn(
        state = state, modifier = modifier
    ) {
        items(smsList) { smsItem ->
            PaymentItem(
                item = smsItem,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 5.dp)
            ) {

            }
        }
    }
}


@Composable
fun PaymentItem(item: AllSMSData, modifier: Modifier = Modifier, onClick: (String) -> Unit) {
    Row(modifier = modifier
        .border(
            width = 1.dp, color = Color.White, shape = RoundedCornerShape(10.dp)
        )
        .background(Color(0xffDCFCE7), shape = RoundedCornerShape(10.dp))
        .clickable {
            onClick(item.id.toString())
        }
        .padding(10.dp)) {
        Column(
            verticalArrangement = Arrangement.Center, modifier = Modifier.weight(.9f)
        ) {
            Text(
                text = item.number, style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.message, style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black,
                ), maxLines = 6, overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = convertIsoToCustomFormat(item.updated_at),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Black,
            ),
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )

    }
}
