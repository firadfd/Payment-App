package fd.firad.paymentapp.home.sms.presentation

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.common.presentation.CustomTopBar
import fd.firad.paymentapp.home.sms.presentation.viewmodel.SMSViewModel


@Composable
fun ProfileScreen(navController: NavHostController, viewModel: SMSViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    var image by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var userStatus by remember { mutableStateOf(false) }
    var dataLoaded by rememberSaveable { mutableStateOf(false) }

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
                            if (state.data.subscriptions.api[0].api_key != null && state.data.subscriptions.api[0].secret_key != null) {
                                viewModel.saveApiToken(state.data.subscriptions.api[0].api_key!!)
                                viewModel.saveSecretKey(state.data.subscriptions.api[0].secret_key!!)
                            }
                            userStatus = viewModel.getApiToken() != null
                        }
                    }
                }
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        CustomTopBar(title = "Profile") {
            navController.navigate("ScreenHome") {
                popUpTo("ScreenHome") {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Box(modifier = Modifier.size(80.dp)) {
            AsyncImage(model = image,
                contentDescription = "User Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape)
                    .clickable { }
                    .background(Color.Gray))

            if (userStatus) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Verified Badge",
                    tint = Color(0xFF1C96E8),
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.BottomEnd)
                        .clip(shape = CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = name,
            style = TextStyle(color = Color(0xFF1C96E8)),
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        ProfileItem(
            icon = Icons.Default.Lock,
            title = "Change Password",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

        }
        Spacer(modifier = Modifier.height(10.dp))
        ProfileItem(
            icon = Icons.Default.Lock,
            title = "Change Password",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

        }
        Spacer(modifier = Modifier.height(10.dp))
        ProfileItem(
            icon = Icons.Default.Lock,
            title = "Change Password",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

        }
        Spacer(modifier = Modifier.height(10.dp))
        ProfileItem(
            icon = Icons.Default.Lock,
            title = "Change Password",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

        }
        Spacer(modifier = Modifier.height(10.dp))
        ProfileItem(
            icon = Icons.Default.Lock,
            title = "Change Password",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

        }


    }
}


@Composable
fun ProfileItem(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .height(60.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = Color(0xFF1F2937))
            .padding(10.dp)
            .clickable { onClick.invoke() }
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.Green.copy(alpha = 0.5f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = TextStyle(color = Color.White),
            fontSize = 18.sp,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.NavigateNext,
            contentDescription = null,
            tint = Color.Green.copy(alpha = 0.5f)
        )
    }

}
