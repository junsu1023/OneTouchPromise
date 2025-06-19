package com.example.onetouchpromise.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.onetouchpromise.R
import com.example.onetouchpromise.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.uiState

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text(text = stringResource(R.string.email)) }
        )

        TextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text(text = stringResource(R.string.password)) }
        )

        Button(
            onClick = { viewModel.signUp() }
        ) {
            Text(text = stringResource(R.string.signup))
        }

        if(state.isLoading) CircularProgressIndicator()

        state.errorMessage?.let { message ->
            Text(
                text = "${stringResource(R.string.error)}: $message",
                color = Color.Red
            )
        }

        if(state.isSuccess) Text(text = stringResource(R.string.complete_signup))
    }
}