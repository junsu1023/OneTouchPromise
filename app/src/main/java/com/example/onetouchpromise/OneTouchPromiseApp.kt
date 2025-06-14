package com.example.onetouchpromise

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.core.viewmodel.BaseViewModel
import com.example.onetouchpromise.navigation.OneTouchPromiseNavHost
import com.example.onetouchpromise.navigation.OneTouchPromiseScreen
import com.example.onetouchpromise.scaffold.FloatingButton
import com.example.onetouchpromise.scaffold.TopBar
import com.example.onetouchpromise.ui.theme.OneTouchPromiseTheme

@Composable
fun OneTouchPromiseApp() {
    val navController = rememberNavController()
    val currentScreen by BaseViewModel.currentScreen.collectAsState(OneTouchPromiseScreen.HOME)

    OneTouchPromiseTheme {
        Scaffold(
            topBar = { TopBar(currentScreen) },
            bottomBar = { /* TODO */ },
            floatingActionButton = {
                FloatingButton(
                    currentScreen = currentScreen,
                    onClick = {
                        navController.navigate(OneTouchPromiseScreen.CREATE_MEETING)
                        BaseViewModel.setCurrentScreen(OneTouchPromiseScreen.CREATE_MEETING)
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                OneTouchPromiseNavHost(
                    navController = navController
                )
            }
        }
    }
}