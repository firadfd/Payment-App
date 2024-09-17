package fd.firad.paymentapp.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import fd.firad.paymentapp.auth.presentation.ForgetPasswordOTPScreen
import fd.firad.paymentapp.auth.presentation.ForgetPasswordScreen
import fd.firad.paymentapp.auth.presentation.LoginScreen
import fd.firad.paymentapp.auth.presentation.RegistrationScreen

@Composable
fun AuthNavHost(rootNavController: NavHostController) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = ScreenLogin) {
        composable<ScreenLogin> {
            LoginScreen(navController, isLoggedIn = {
                rootNavController.navigate("MainNavHost") {
                    popUpTo("AuthNavHost") { inclusive = true }
                }
            })
        }
        composable<ScreenRegistration> {
            RegistrationScreen(navController)
        }
        composable<ScreenForgetPassword> {
            ForgetPasswordScreen(navController)
        }
        composable<ScreenForgetPasswordOTP> {
            val args = it.toRoute<ScreenForgetPasswordOTP>()
            val email = args.email
            ForgetPasswordOTPScreen(navController, email)
        }


    }


}