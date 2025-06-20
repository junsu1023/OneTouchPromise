package com.example.onetouchpromise.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.error.AuthException
import com.example.onetouchpromise.R
import com.example.onetouchpromise.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onSignUpSuccess: () -> Unit
) {
    val state = viewModel.uiState

    LaunchedEffect(state.isSuccess) {
        if(state.isSuccess) {
            onSignUpSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.signup),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text(text = stringResource(R.string.email)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = viewModel::onEmailChange,
                label = { Text(text = stringResource(R.string.password)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.signUp() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = stringResource(R.string.accession))
                }
            }

            val errorMessage = when(state.error) {
                is AuthException.EmailFormatInvalid -> stringResource(R.string.invalid_email)
                is AuthException.PasswordTooShort -> stringResource(R.string.password_too_short)
                is AuthException.UserNotFound -> stringResource(R.string.user_not_found)
                is AuthException.WrongPassword -> stringResource(R.string.wrong_password)
                is AuthException.Unknown -> state.error.message ?: stringResource(R.string.unknown)
                null -> null
            }

            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (state.isSuccess) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.complete_signup),
                    color = colorResource(R.color.apple),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}