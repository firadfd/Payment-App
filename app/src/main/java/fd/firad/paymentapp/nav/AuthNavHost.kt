package fd.firad.paymentapp.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fd.firad.paymentapp.auth.presentation.LoginScreen
import fd.firad.paymentapp.auth.presentation.RegistrationScreen

@Composable
fun AuthNavHost(rootNavController: NavHostController) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = ScreenLogin) {
        composable<ScreenLogin> {
            LoginScreen(navController,isLoggedIn = {
                rootNavController.navigate("MainNavHost") {
                    popUpTo("AuthNavHost") { inclusive = true }
                }
            })
        }
        composable<ScreenRegistration> {
            RegistrationScreen(navController)
        }
    }


}