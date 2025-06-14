package com.example.onetouchpromise

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.onetouchpromise.navigation.OneTouchPromiseNavHost
import com.example.onetouchpromise.ui.theme.OneTouchPromiseTheme

@Composable
fun OneTouchPromiseApp() {
    val navController = rememberNavController()

    OneTouchPromiseTheme {
        Scaffold(
            topBar = { /* TODO */ },
            bottomBar = { /* TODO */ },
            floatingActionButton = { /* TODO */ }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                OneTouchPromiseNavHost(navController)
            }
        }
    }
}