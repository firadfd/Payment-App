package fd.firad.paymentapp.home.sms.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import fd.firad.paymentapp.common.presentation.CustomTopBar
import fd.firad.paymentapp.home.sms.presentation.viewmodel.SMSViewModel


@Composable
fun FailedSMSScreen(navController: NavHostController, viewModel: SMSViewModel = hiltViewModel()) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        CustomTopBar(title = "Failed Payment SMS") {
            navController.navigate("ScreenHome") {
                popUpTo("ScreenHome") {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }
}
