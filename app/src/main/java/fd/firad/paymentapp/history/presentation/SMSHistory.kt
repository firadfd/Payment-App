package fd.firad.paymentapp.history.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fd.firad.paymentapp.common.constants.Constants.convertIsoToCustomFormat
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.home.sms.data.model.AllSMSData
import fd.firad.paymentapp.home.sms.presentation.viewmodel.SMSViewModel


@Composable
fun SMSHistory(modifier: Modifier = Modifier, viewModel: SMSViewModel = hiltViewModel()) {
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
            SMSItem(
                smsModelItem = smsItem,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 5.dp, vertical = 5.dp), onClick = {

                }
            )
        }
    }
}

@Composable
fun SMSItem(
    smsModelItem: AllSMSData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .background(
                color = Color(0xfff9f9f9),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                onClick()
            }
            .padding(10.dp)

    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(.65f)
        ) {
            Text(
                text = smsModelItem.number,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = smsModelItem.message,
                style = TextStyle(
                    color = Color.Gray,
                ),
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .weight(.35f)
        ) {
            Text(
                text = convertIsoToCustomFormat(smsModelItem.updated_at),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray,
                    textAlign = TextAlign.End
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = smsModelItem.status.toUpperCase(),
                style = TextStyle(
                    color = if (smsModelItem.status == "sent") Color.Blue else Color.Red,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
        }
    }
}