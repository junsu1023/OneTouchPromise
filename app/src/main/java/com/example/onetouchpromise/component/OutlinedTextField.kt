package com.example.onetouchpromise.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.onetouchpromise.R

@Composable
fun EmailOutlinedTextField(
    email: String,
    onEmailChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = email,
        onValueChange = { onEmailChange(it) },
        placeholder = { Text(text = stringResource(R.string.email)) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        trailingIcon = {
            if(email.isNotEmpty()) {
                IconButton(
                    onClick = { onEmailChange("") }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.delete_all)
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(R.color.outlined_focused_border),
            unfocusedBorderColor = colorResource(R.color.outlined_focused_border).copy(alpha = 0.3f)
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    )
}

@Composable
fun NickNameOutlinedTextField(
    nickname: String,
    onNicknameChange: (String) -> Unit
) {
    OutlinedTextField(
        value = nickname,
        onValueChange = { onNicknameChange(it) },
        label = { Text(text = stringResource(R.string.nickname)) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        trailingIcon = {
            if(nickname.isNotEmpty()) {
                IconButton(
                    onClick = { onNicknameChange("") }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.delete_all)
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(R.color.outlined_focused_border),
            unfocusedBorderColor = colorResource(R.color.outlined_focused_border).copy(alpha = 0.3f)
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        )
    )
}

@Composable
fun PasswordOutlinedTextField(
    password: String,
    passwordVisible: Boolean,
    onPasswordChange: (String) -> Unit,
    onVisibleChange: (Boolean) -> Unit
) {
    OutlinedTextField(
        value = password,
        onValueChange = { onPasswordChange(it) },
        placeholder = { Text(text = stringResource(R.string.password)) },
        visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        trailingIcon = {
            val icon = if(passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
            val description = if(passwordVisible) stringResource(R.string.hide_password) else stringResource(R.string.show_password)

            if(password.isNotEmpty()) {
                IconButton(
                    onClick = { onVisibleChange(!passwordVisible) }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = description
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(R.color.outlined_focused_border),
            unfocusedBorderColor = colorResource(R.color.outlined_focused_border).copy(alpha = 0.3f)
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )
    )
}