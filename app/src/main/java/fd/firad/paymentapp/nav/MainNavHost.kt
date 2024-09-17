package fd.firad.paymentapp.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.SmsFailed
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fd.firad.paymentapp.home.sms.presentation.FailedSMSScreen
import fd.firad.paymentapp.home.sms.presentation.HomeScreen
import fd.firad.paymentapp.home.sms.presentation.ProfileScreen
import fd.firad.paymentapp.home.sms.presentation.SMSHistoryScreen
import fd.firad.paymentapp.home.sms.presentation.SMSScreen
import fd.firad.paymentapp.ui.theme.yellowColor

@Composable
fun MainNavHost(rootNavController: NavHostController) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val itemList = listOf(
        BottomNavigationItem(
            title = "Home", routeName = "ScreenHome", selectedIcon = Icons.Default.Home
        ),
        BottomNavigationItem(
            title = "SMS", routeName = "ScreenSMS", selectedIcon = Icons.Default.Sms
        ),
        BottomNavigationItem(
            title = "Failed",
            routeName = "ScreenPaymentSMS",
            selectedIcon = Icons.Default.SmsFailed
        ),
        BottomNavigationItem(
            title = "History",
            routeName = "ScreenSMSHistory",
            selectedIcon = Icons.Default.History
        ),
        BottomNavigationItem(
            title = "Profile",
            routeName = "ScreenProfile",
            selectedIcon = Icons.Default.Person3
        ),
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Color.Black, bottomBar = {
            if (currentDestination != null) {
                NavigationBar(
                    modifier = Modifier
                        .background(color = Color.White)
                        .clip(
                            shape = RoundedCornerShape(
                                topStart = 10.dp,
                                topEnd = 10.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            )
                        )
                        .fillMaxWidth(),
                    containerColor = Color(0xFF729762)
                ) {
                    itemList.forEachIndexed { index, item ->
                        val isSelected =
                            if (currentDestination.route in itemList.map { it.routeName }) {
                                currentDestination.hierarchy.any { it.route == item.routeName }
                            } else {
                                index == selectedItemIndex // Use last selected index if not in BottomNav
                            }
                        NavigationBarItem(colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        ), selected = isSelected, onClick = {
                            selectedItemIndex = index
                            if (item.routeName == "ScreenHome") {
                                navController.navigate("ScreenHome") {
                                    popUpTo("ScreenHome") { inclusive = true }
                                    launchSingleTop = true
                                }
                            } else {
                                navController.navigate(item.routeName)
                            }

                        }, label = {
                            Text(
                                text = item.title,
                                maxLines = 1,
                                style = TextStyle(
                                    color = if (isSelected) {
                                        yellowColor
                                    } else {
                                        Color.White
                                    }
                                ),

                                )
                        }, icon = {
                            Badge(containerColor = Color.Transparent) {
                                Icon(
                                    item.selectedIcon,
                                    contentDescription = null,
                                    tint = if (isSelected) {
                                        yellowColor
                                    } else {
                                        Color.White
                                    },
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        })
                    }
                }
            }

        }) { paddingValues ->
            NavHost(
                navController,
                startDestination = "ScreenHome",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("ScreenHome") {
                    HomeScreen(navController = navController)
                }
                composable("ScreenSMS") {
                    SMSScreen(navController = navController)
                }
                composable("ScreenPaymentSMS") {
                    FailedSMSScreen(navController = navController)
                }
                composable("ScreenSMSHistory") {
                    SMSHistoryScreen(navController = navController)
                }
                composable("ScreenProfile") {
                    ProfileScreen(navController = navController, isLogout = {
                        rootNavController.navigate("AuthNavHost") {
                            popUpTo("MainNavHost") { inclusive = true }
                        }
                    })
                }
            }
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val routeName: String,
    val selectedIcon: ImageVector,
    val budgeCount: Int? = null
)