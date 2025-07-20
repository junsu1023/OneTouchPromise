package com.example.onetouchpromise.ui

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.onetouchpromise.R
import com.example.onetouchpromise.component.BottomNavigation
import com.example.onetouchpromise.viewmodel.SettingViewModel

@Composable
fun SettingScreen(
    curRoute: String?,
    viewModel: SettingViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    onWithDraw: () -> Unit,
    onFriendshipClick: () -> Unit,
    onHomeClick: () -> Unit,
    onSettingClick: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val updateNicknameState by viewModel.updateNicknameState.collectAsState()
    val changePasswordState by viewModel.changePasswordState.collectAsState()

    val isInformationExpanded = remember { mutableStateOf(false) }
    val showNicknameDialog = remember { mutableStateOf(false) }
    val showPasswordDialog = remember { mutableStateOf(false) }
    val newNickname = remember { mutableStateOf("") }
    val currentPassword = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getCurrentUSer()
    }

    LaunchedEffect(updateNicknameState) {
        updateNicknameState?.let { result ->
            if(result.isSuccess) {
                Toast.makeText(context, getString(context, R.string.success_update_nickname), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, getString(context, R.string.failed_update_nickname), Toast.LENGTH_SHORT).show()
            }

            viewModel.resetNickNameUpdateState()
        }
    }

    LaunchedEffect(changePasswordState) {
        changePasswordState?.let { result ->
            if(result.isSuccess) {
                Toast.makeText(context, getString(context, R.string.again_login), Toast.LENGTH_SHORT).show()
                onLogout()
            } else {
                Toast.makeText(context, getString(context, R.string.failed_change_password), Toast.LENGTH_SHORT).show()
            }

            viewModel.resetPasswordChangeState()
        }
    }

    Scaffold(
        modifier = Modifier.background(colorResource(R.color.main_background)),
        containerColor = colorResource(R.color.main_background),
        contentColor = colorResource(R.color.main_background),
        topBar = {
            SettingScreenTopBar()
        },
        bottomBar = {
            BottomNavigation(
                curRoute = curRoute,
                onFriendshipClick = onFriendshipClick,
                onHomeClick = onHomeClick,
                onSettingClick = onSettingClick
            )
        }
    ) { paddingValues ->
        if(showNicknameDialog.value) {
            ChangeDialog(
                onDismissRequest = { showNicknameDialog.value = false },
                title = stringResource(R.string.change_nickname),
                value = newNickname.value,
                onValueChange = { newNickname.value = it },
                labelText = stringResource(R.string.new_nickname),
                onConfirmClick = {
                    viewModel.apply {
                        updateNickname(newNickname.value)
                        getCurrentUSer()
                    }

                    showNicknameDialog.value = false
                },
                onDismissClick = { showNicknameDialog.value = false }
            )
        }

        if(showPasswordDialog.value) {
            ChangeDialog(
                onDismissRequest = { showPasswordDialog.value = false },
                title = stringResource(R.string.change_password),
                value = newPassword.value,
                value2 = currentPassword.value,
                onValueChange = { newPassword.value = it },
                onValueChange2 = {currentPassword.value = it },
                labelText = stringResource(R.string.new_password),
                labelText2 = stringResource(R.string.current_password),
                onConfirmClick = {
                    viewModel.changePassword(
                        currentPassword = currentPassword.value,
                        newPassword = newPassword.value
                    )
                    showPasswordDialog.value = false
                },
                onDismissClick = { showPasswordDialog.value = false }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.padding(start = 20.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.my_information),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colorResource(R.color.basic_text_color2)
                        )
                    )

                    IconButton(
                        onClick = { isInformationExpanded.value = !isInformationExpanded.value }
                    ) {
                        Icon(
                            imageVector = if(isInformationExpanded.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = if(isInformationExpanded.value) stringResource(R.string.collapse_my_information) else stringResource(R.string.expand_my_information),
                            tint = colorResource(R.color.basic_icon_color),
                        )
                    }
                }

                if(isInformationExpanded.value) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Text(
                            text = "${stringResource(R.string.email)}:  ${uiState?.email ?: stringResource(R.string.invalid_email)}",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = colorResource(R.color.basic_text_color2)
                            )
                        )

                        Text(
                            text = "${stringResource(R.string.nickname)}:  ${uiState?.nickname ?: stringResource(R.string.invalid_nickname)}",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = colorResource(R.color.basic_text_color2)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = stringResource(R.string.change_nickname),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(R.color.basic_text_color2)
                    ),
                    modifier = Modifier.clickable { showNicknameDialog.value = true }
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = stringResource(R.string.change_password),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(R.color.basic_text_color2)
                    ),
                    modifier = Modifier.clickable { showPasswordDialog.value = true }
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = stringResource(R.string.logout),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(R.color.basic_text_color2)
                    ),
                    modifier = Modifier.clickable {
                        onLogout()
                    }
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = stringResource(R.string.withdraw),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(R.color.basic_text_color2)
                    ),
                    modifier = Modifier.clickable {
                        onWithDraw()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreenTopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.main_background)),
        title = {
            Text(
                text = stringResource(R.string.setting),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.basic_text_color)
                )
            )
        }
    )
}

@Composable
fun ChangeDialog(
    onDismissRequest: () -> Unit,
    title: String,
    value: String,
    value2: String = "",
    onValueChange: (String) -> Unit,
    onValueChange2: ((String) -> Unit) = { },
    labelText: String,
    labelText2: String = "",
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = title,
                color = colorResource(R.color.basic_text_color2)
            )
        },
        text = {
            Column {
                if(title == stringResource(R.string.change_password)) {
                    OutlinedTextField(
                        value = value2,
                        onValueChange = { onValueChange2(it) },
                        label = {
                            Text(
                                text = labelText2,
                                color = colorResource(R.color.gray)
                            )
                        },
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = value,
                    onValueChange = { onValueChange(it) },
                    label = {
                        Text(
                            text = labelText,
                            color = colorResource(R.color.gray)
                        )
                    },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmClick
            ) {
                Text(
                    text = stringResource(R.string.change)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissClick
            ) {
                Text(
                    text = stringResource(R.string.cancel)
                )
            }
        }
    )
}