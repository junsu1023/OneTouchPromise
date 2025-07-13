package com.example.onetouchpromise.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.onetouchpromise.R
import com.example.onetouchpromise.component.EmailOutlinedTextField
import com.example.onetouchpromise.component.NickNameOutlinedTextField
import com.example.onetouchpromise.component.PasswordOutlinedTextField
import com.example.onetouchpromise.error.getAuthError
import com.example.onetouchpromise.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onSignUpSuccess: () -> Unit
) {
    val state = viewModel.uiState
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if(state.isSuccess) {
            onSignUpSuccess()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(R.color.login_background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 32.dp,
                    vertical = 48.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(R.string.signup),
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.basic_text_color2)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            EmailOutlinedTextField(
                email = state.email,
                onEmailChange = { viewModel.onEmailChange(it) }
            )

            NickNameOutlinedTextField(
                nickname = state.nickname,
                onNicknameChange = { viewModel.onNickNameChange(it) }
            )

            PasswordOutlinedTextField(
                password = state.password,
                passwordVisible = passwordVisible,
                onPasswordChange = { viewModel.onPasswordChange(it) },
                onVisibleChange = { passwordVisible = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.signUp() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.outlined_focused_border)),
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

            val errorMessage = getAuthError(state.error)

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