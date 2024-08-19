package fd.firad.paymentapp.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun RootNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "AuthNavHost") {
        composable("AuthNavHost") {
            AuthNavHost(navController)
        }
        composable("MainNavHost") {
            MainNavHost(navController)
        }
    }

}

val NavHostController.canGoBack: Boolean
    get() = currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

fun NavHostController.navigateBack() {
    if (canGoBack) {
        popBackStack()
    }
}