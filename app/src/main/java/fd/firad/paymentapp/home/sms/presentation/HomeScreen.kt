package fd.firad.paymentapp.home.sms.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmsFailed
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.home.sms.presentation.viewmodel.SMSViewModel
import fd.firad.paymentapp.service.SmsService


@Composable
fun HomeScreen(navController: NavHostController, viewModel: SMSViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
    var emailStatus by remember { mutableStateOf(true) }
    var dataLoaded by rememberSaveable { mutableStateOf(false) }
    val packageName = context.packageName
    val powerManager = remember {
        context.getSystemService(Context.POWER_SERVICE) as PowerManager
    }

    val batteryOptimizationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (powerManager.isIgnoringBatteryOptimizations(packageName)) {
        } else {

        }
    }

    LaunchedEffect(Unit) {
        if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = Uri.parse("package:$packageName")
            }
            batteryOptimizationLauncher.launch(intent)
        }
    }

    LaunchedEffect(Unit) {
        startSmsService(context)
    }

    if (!dataLoaded) {
        LaunchedEffect(viewModel) {
            viewModel.userInfo()
        }
    }

    if (!dataLoaded) {
        LaunchedEffect(viewModel) {
            viewModel.userInfoState.collect { state ->
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
                        if (state.data.status) {
                            name = state.data.user.name
                            image = state.data.user.profile_image
                                ?: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT-qGN4p3D0Vvl6F3tg37qRWmknI78ka5nyut0Vrf6xelsLnB3-weNxq13bHjN-s5hvSNE&usqp=CAU"
//                            emailStatus = state.data.user.email_verified_at != null
                            if (state.data.subscriptions.api[0].api_key != null && state.data.subscriptions.api[0].secret_key != null) {
                                viewModel.saveApiToken(state.data.subscriptions.api[0].api_key!!)
                                viewModel.saveSecretKey(state.data.subscriptions.api[0].secret_key!!)
                            }
                        }
                    }
                }
            }
        }
    }


    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().background(color = Color.White)
    ) {
        PinnedChatsTopBar(
            name = name,
            image = image,
            emailStatus = emailStatus,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        ) {

        }
        Spacer(modifier = Modifier.height(10.dp))
        SMSStatusItem(
            Icons.Default.AccessTime,
            "Pending SMS",
            "",
            "10",
            Color(0xffDBEAFE),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        SMSStatusItem(
            Icons.Default.Send,
            "Send SMS",
            "",
            "4",
            Color(0xffDCFCE7),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        SMSStatusItem(
            Icons.Default.SmsFailed,
            "Failed Payment SMS",
            "",
            "12",
            Color(0xffFEF9C3),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))


    }
}

fun startSmsService(context: Context) {
    val intent = Intent(context, SmsService::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else {
        context.startService(intent)
    }
}


@Composable
fun SMSStatusItem(
    icon: ImageVector,
    title: String,
    subTitle: String,
    amount: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Checkmark",
                tint = Color(0xFF00C853),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = Color(0xFF4D4D4D)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = amount,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000)
                )
            }

        }
    }
}

@Composable
fun PinnedChatsTopBar(
    name: String,
    image: String,
    emailStatus: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(color = Color(0xFF1F2937))
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Start
        )
        Box(modifier = Modifier.size(48.dp)) {
            AsyncImage(model = image,
                contentDescription = "User Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape)
                    .clickable { onClick() }
                    .background(Color.Gray))

            if (emailStatus) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Verified Badge",
                    tint = Color(0xFF1C96E8),
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.BottomEnd)
                        .clip(shape = CircleShape)
                )
            }
        }
    }
}
