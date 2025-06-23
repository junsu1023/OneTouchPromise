package com.example.onetouchpromise.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.example.onetouchpromise.navigation.OneTouchPromiseScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun DecideStartDestination(
    navController: NavHostController
) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    LaunchedEffect(Unit) {
        if(currentUser != null) {
            navController.navigate(OneTouchPromiseScreen.HOME) {
                popUpTo(0) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(OneTouchPromiseScreen.LOGIN) {
                popUpTo(0) {
                    inclusive = true
                }
            }
        }
    }
}