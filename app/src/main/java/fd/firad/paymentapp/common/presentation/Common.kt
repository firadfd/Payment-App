package fd.firad.paymentapp.common.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomButton(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    textColor: Color = Color.Black,
    cornerRadius: Dp = 5.dp,
    onClick: () -> Unit
) {
    Surface(shape = RoundedCornerShape(cornerRadius), color = color, modifier = modifier.clickable {
        onClick.invoke()
    }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
        ) {

            Text(
                text = text,
                fontSize = 16.sp,
                color = textColor,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp)
            )

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    enable: Boolean = true,
    singleLine: Boolean = true,
    placeholder: String = "",
    errorMessage: String? = null
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(value = value,
            onValueChange = onValueChange,
            enabled = enable,
            readOnly = !enable,
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
                .background(Color(0xFF2D3748))
                .border(
                    width = if (isFocused) 1.dp else 0.dp,
                    color = if (isFocused) Color(0xFF9CA3AF) else Color.Transparent,
                    shape = RoundedCornerShape(5.dp)
                )
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.White,
                cursorColor = Color.White
            ),
            placeholder = {
                Text(
                    placeholder,
                    style = TextStyle(),
                )
            },
            textStyle = TextStyle(color = Color.White),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction, keyboardType = keyboardType
            ),
            singleLine = singleLine
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
    }

}

@Composable
fun PriceInputField(price: String, onIncressClicked: () -> Unit, ondecressClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2D3748), RoundedCornerShape(5.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp), contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(modifier = Modifier.width(5.dp))
            Icon(Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                modifier = Modifier
                    .clickable { ondecressClicked() }
                    .padding(10.dp),
                tint = Color(0xFF6C757D))
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = price,
                color = Color(0xFF9CA3AF),
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier
                    .clickable { onIncressClicked() }
                    .padding(10.dp),
                tint = Color(0xFF6C757D))
            Spacer(modifier = Modifier.width(5.dp))

        }
    }
}


@Composable
fun CustomUserNameInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF2D3748),
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    placeholder: String = ""
) {
    OutlinedTextField(
        value = value,
        readOnly = true,
        enabled = false,
        onValueChange = onValueChange,
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp, color = Color.Transparent, shape = RoundedCornerShape(5.dp)
            ),
        colors = OutlinedTextFieldDefaults.colors(Color.Black),
        placeholder = {
            Text(
                placeholder,
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction, keyboardType = keyboardType
        ),
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextInputTailedField(
    value: String,
    onValueChange: (String) -> Unit,
    verified: Boolean,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    placeholder: String = "",
    onTailedClickChange: () -> Unit
) {
    OutlinedTextField(value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .background(Color(0xFF2D3748))
            .border(
                width = 0.dp, color = Color.Transparent, shape = RoundedCornerShape(5.dp)
            )
            .fillMaxWidth(),
        enabled = false,
        readOnly = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = Color.White,
            cursorColor = Color.White
        ),
        placeholder = {
            Text(
                placeholder,
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction, keyboardType = keyboardType
        ),
        singleLine = true,
        trailingIcon = {
            Surface(
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clickable {
                        if (!verified) {
                            onTailedClickChange.invoke()
                        }
                    },
            ) {
                Text(
                    text = if (verified) "verified" else "unverified",
                    modifier = Modifier.padding(5.dp),
                    maxLines = 1,
                    style = TextStyle(
                        color = if (verified) Color.Black else Color.White, fontSize = 12.sp
                    ),

                    )
            }
        })
}

@Composable
fun CustomPickImage(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    placeholder: String = "",
    onClick: () -> Unit
) {
    OutlinedTextField(value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .background(Color(0xFF2D3748))
            .border(
                width = 1.dp, color = Color.Transparent, shape = RoundedCornerShape(5.dp)
            ),
        colors = OutlinedTextFieldDefaults.colors(Color.Black),
        placeholder = {
            Text(
                "",
            )
        },
        enabled = false,
        readOnly = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction, keyboardType = keyboardType
        ),
        singleLine = true,
        trailingIcon = {
            Surface(
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .padding(all = 10.dp)
                    .clickable {
                        onClick.invoke()
                    },
            ) {
                Text(
                    text = placeholder,
                    modifier = Modifier.padding(10.dp),
                    maxLines = 1,
                    style = TextStyle(
                        color = Color.White, fontSize = 12.sp
                    ),

                    )
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOTPInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    onResendCodeClick: () -> Unit,
    errorMessage: String? = null
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
                .background(Color(0xFF2D3748))
                .border(
                    width = if (isFocused) 1.dp else 0.dp,
                    color = if (isFocused) Color(0xFF9CA3AF) else Color.Transparent,
                    shape = RoundedCornerShape(5.dp)
                )
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.White,
                cursorColor = Color.White
            ),
            placeholder = {
                Text(
                    placeholder,
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done, keyboardType = KeyboardType.Text
            ),
            trailingIcon = {
                Text("Resend Code",
                    color = Color(0xFFF59E0B),
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable {
                            onResendCodeClick.invoke()
                        })
            },
            singleLine = true
        )
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hintText: String = "",
    errorMessage: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        OutlinedTextField(value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = hintText, style = TextStyle()) },
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
                .background(Color(0xFF2D3748))
                .border(
                    width = if (isFocused) 1.dp else 0.dp,
                    color = if (isFocused) Color(0xFF9CA3AF) else Color.Transparent,
                    shape = RoundedCornerShape(5.dp)
                )
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.White,
                cursorColor = Color.White
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            })
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewButton() {
    CustomOTPInputField(value = "", onValueChange = {

    }, onResendCodeClick = {

    }, modifier = Modifier.fillMaxWidth(.9f))
}

@Composable
fun GoogleButton(
    modifier: Modifier = Modifier, cornerRadius: Dp = 5.dp, onClick: () -> Unit
) {
    Surface(shape = RoundedCornerShape(cornerRadius), modifier = modifier.clickable { onClick() }) {
        Box(modifier = Modifier.clickable { onClick() }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .align(Alignment.Center)
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Continue with Google", maxLines = 1, style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                    ), modifier = Modifier.wrapContentWidth()
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun CustomEarnButton(
    text: String,
    textColor: Color,
    iconTint: Color,
    modifier: Modifier = Modifier,
    showIcon: Boolean = true,
    icon: Int,
    cornerRadius: Dp = 5.dp,
    onClick: () -> Unit
) {
    Surface(shape = RoundedCornerShape(cornerRadius), modifier = modifier.clickable { onClick() }) {
        Box(modifier = Modifier.clickable { onClick() }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .align(Alignment.Center)
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (showIcon) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = text, maxLines = 1, style = TextStyle(
                            color = textColor,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                        ), modifier = Modifier.wrapContentWidth()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = text, maxLines = 1, style = TextStyle(
                            color = textColor,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                        ), modifier = Modifier.wrapContentWidth()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }

            }
        }
    }
}

@Composable
fun CustomTopBar(
    title: String,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.White,
    icon: ImageVector = Icons.Default.ArrowBack,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .height(70.dp)
            .background(color = Color(0xFF729762))
            .padding(10.dp)
            .fillMaxWidth()
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp, end = 10.dp)
                .clickable {
                    onClick.invoke()
                },
            tint = iconTint
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = title, maxLines = 1, style = TextStyle(
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
            ), modifier = Modifier.wrapContentWidth()
        )
        Spacer(modifier = Modifier.weight(1f))

    }
}


@Composable
fun LoadingScreen(modifier: Modifier = Modifier, title: String = "Loading...") {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White), contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(color = Color.Black)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                style = TextStyle(color = Color.Black),
                fontSize = 20.sp,
            )
        }
    }
}


