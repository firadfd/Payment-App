package fd.firad.paymentapp.home.sms.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import fd.firad.paymentapp.common.presentation.CustomTopBar
import fd.firad.paymentapp.home.sms.presentation.viewmodel.SMSViewModel

@Composable
fun SMSHistoryScreen(navController: NavHostController, viewModel: SMSViewModel = hiltViewModel()) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().background(color = Color.White)
    ) {
        CustomTopBar(title = "SMS History") {
            navController.navigate("ScreenHome") {
                popUpTo("ScreenHome") {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }
}
