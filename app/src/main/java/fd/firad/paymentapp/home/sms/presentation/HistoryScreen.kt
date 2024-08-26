package fd.firad.paymentapp.home.sms.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import fd.firad.paymentapp.common.presentation.CustomTopBar
import fd.firad.paymentapp.history.presentation.PaymentHistory
import fd.firad.paymentapp.history.presentation.SMSHistory
import fd.firad.paymentapp.home.sms.presentation.viewmodel.SMSViewModel

@Composable
fun SMSHistoryScreen(navController: NavHostController, viewModel: SMSViewModel = hiltViewModel()) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        CustomTopBar(title = "History") {
            navController.navigate("ScreenHome") {
                popUpTo("ScreenHome") {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        ViewPagerHistoryTabs(Modifier.weight(1f))
    }
}

@Composable
private fun ViewPagerHistoryTabs(modifier: Modifier = Modifier) {
    val tabs = listOf("SMS History", "Payment History")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(modifier = modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            contentColor = Color.White,
            divider = {},
            indicator = { tabPositions ->
                if (tabPositions.isNotEmpty()) {
                    val selectedTabPosition = tabPositions[selectedTabIndex]
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(selectedTabPosition)
                            .height(1.dp)
                            .background(Color.Red)
                    )
                }
            }) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTabIndex == index) Color.Red else Color.Black,
                            fontSize = 14.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // Ensure each tab occupies equal width
                )
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedTabIndex) {
                0 -> SMSHistory(modifier)
                1 -> PaymentHistory(modifier)
            }
        }
    }
}
