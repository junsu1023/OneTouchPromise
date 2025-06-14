package com.example.onetouchpromise.scaffold

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.example.onetouchpromise.navigation.OneTouchPromiseScreen

@Composable
fun FloatingButton(
    currentScreen: String,
    onClick: () -> Unit
) {
    if(currentScreen == OneTouchPromiseScreen.HOME) {
        FloatingActionButton(
            onClick = { onClick() },
            content = {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "모임 생성"
                )
            }
        )
    }
}