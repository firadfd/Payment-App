package fd.firad.paymentapp.home.sms.presentation

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.common.presentation.CustomButton
import fd.firad.paymentapp.common.presentation.CustomTopBar
import fd.firad.paymentapp.home.sms.presentation.viewmodel.SMSViewModel
import fd.firad.paymentapp.nav.ScreenPassChange
import fd.firad.paymentapp.ui.theme.blueColor
import fd.firad.paymentapp.ui.theme.textColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: SMSViewModel = hiltViewModel(),
    isLogout: () -> Unit
) {
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    var imageUrl by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var userStatus by remember { mutableStateOf(false) }
    var dataLoaded by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    // Image picker launcher
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            launcher.launch("image/*")
        } else {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        }
    }

    fun pickImage() {
        when {
            // For Android 13+ (SDK 33+)
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU -> {
                permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            }

            // For Android 10 to 12 (SDK 29 to 32)
            android.os.Build.VERSION.SDK_INT in android.os.Build.VERSION_CODES.Q..android.os.Build.VERSION_CODES.S -> {
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            // For Android 6.0 to Android 9 (SDK 23 to 28)
            else -> {
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFF1F2937)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Are you sure to logout?",
                    modifier = Modifier.fillMaxWidth(.9f),
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        color = textColor, fontSize = 18.sp
                    )
                )
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(.9f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    CustomButton(
                        text = "Cancel",
                        color = blueColor,
                        modifier = Modifier.weight(1f),
                        textColor = Color.White,
                        cornerRadius = 5.dp
                    ) {
                        showBottomSheet = false
                    }
                    Spacer(modifier = Modifier.weight(.1f))
                    CustomButton(
                        text = "Logout",
                        color = Color(0xFFEF4444),
                        modifier = Modifier.weight(1f),
                        textColor = Color.White,
                        cornerRadius = 5.dp
                    ) {
                        showBottomSheet = false
                        viewModel.deleteToken()
                        viewModel.deleteApiToken()
                        viewModel.deleteSecretKey()
                        isLogout.invoke()
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
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
                            imageUrl = state.data.user.profile_image
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
            AsyncImage(model = selectedImageUri ?: imageUrl,
                contentDescription = "User Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape)
                    .clickable {
                       pickImage()
                    }
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
            icon = Icons.Default.Edit,
            title = "Change Name",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

        }
        Spacer(modifier = Modifier.height(10.dp))
        ProfileItem(
            icon = Icons.Default.Email,
            title = "Change Email",
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
        navController.navigate(ScreenPassChange)
        }
        Spacer(modifier = Modifier.height(10.dp))
        ProfileItem(
            icon = Icons.Default.Logout,
            title = "Log Out",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            showBottomSheet = true
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
