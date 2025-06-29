package com.example.onetouchpromise.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.onetouchpromise.contract.SplashUiState
import com.example.onetouchpromise.R
import com.example.onetouchpromise.component.ShimmeringImage
import com.example.onetouchpromise.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState = viewModel.uiState
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(uiState) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1200)
        )

        delay(1500)

        when(uiState) {
            SplashUiState.NavigateToHome -> onNavigateToHome()
            SplashUiState.NavigateToLogin -> onNavigateToLogin()
            else -> { /* Do not Anything */ }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.pastel_blue)),
        contentAlignment = Alignment.Center
    ) {
        ShimmeringImage(
            painter = painterResource(id = R.drawable.splash),
            modifier = Modifier.size(200.dp),
            alpha = alpha.value
        )
    }
}