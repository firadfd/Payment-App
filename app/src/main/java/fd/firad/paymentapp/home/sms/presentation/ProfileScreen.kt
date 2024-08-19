package fd.firad.paymentapp.home.sms.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import fd.firad.paymentapp.common.presentation.CustomTopBar
import fd.firad.paymentapp.home.sms.presentation.viewmodel.SMSViewModel
import fd.firad.paymentapp.nav.navigateBack


@Composable
fun ProfileScreen(navController: NavHostController, viewModel: SMSViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    var dataLoaded by rememberSaveable { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        CustomTopBar(title = "Profile") {
            navController.navigate("ScreenHome") {
                popUpTo("ScreenHome") {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }
}
