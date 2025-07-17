package com.example.onetouchpromise.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.UserModel
import com.example.onetouchpromise.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ModalDrawerScreen(
    scope: CoroutineScope,
    drawerState: DrawerState,
    currentUser: UserModel?,
    onLogoutClick: () -> Unit,
    onWithDraw: () -> Unit,
    onChangeNickname: (String) -> Unit,
    onChangePassword: (String) -> Unit
) {
    val isInformationExpanded = remember { mutableStateOf(false) }
    val showNicknameDialog = remember { mutableStateOf(false) }
    val showPasswordDialog = remember { mutableStateOf(false) }
    val newNickname = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }

    if(showNicknameDialog.value) {
        ChangeDialog(
            onDismissRequest = { showNicknameDialog.value = false },
            title = stringResource(R.string.change_nickname),
            value = newNickname.value,
            onValueChange = { newNickname.value = it },
            onConfirmClick = {
                onChangeNickname(newNickname.value)
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
            onValueChange = { newPassword.value = it },
            onConfirmClick = {
                onChangePassword(newPassword.value)
                showPasswordDialog.value = false
            },
            onDismissClick = { showPasswordDialog.value = false }
        )
    }

    ModalDrawerSheet {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.go_back),
                    tint = colorResource(R.color.basic_icon_color)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(R.string.setting),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.basic_text_color)
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

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
                        text = "${stringResource(R.string.email)}:  ${currentUser?.email ?: stringResource(R.string.invalid_email)}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(R.color.basic_text_color2)
                        )
                    )

                    Text(
                        text = "${stringResource(R.string.nickname)}:  ${currentUser?.nickname ?: stringResource(R.string.invalid_nickname)}",
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
                    scope.launch {
                        drawerState.close()
                    }
                    onLogoutClick()
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
                    scope.launch {
                        drawerState.close()
                    }
                    onWithDraw()
                }
            )
        }
    }
}

@Composable
fun ChangeDialog(
    onDismissRequest: () -> Unit,
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
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
                OutlinedTextField(
                    value = value,
                    onValueChange = { onValueChange(it) },
                    label = {
                        Text(
                            text = stringResource(R.string.new_nickname),
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