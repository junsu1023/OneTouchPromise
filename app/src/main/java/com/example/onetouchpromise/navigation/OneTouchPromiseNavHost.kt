package com.example.onetouchpromise.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.onetouchpromise.ui.CreateMeetingScreen
import com.example.onetouchpromise.ui.HomeScreen
import com.example.onetouchpromise.ui.LoginScreen
import com.example.onetouchpromise.ui.MeetingDetailScreen
import com.example.onetouchpromise.ui.SignUpScreen

@Composable
fun OneTouchPromiseNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = OneTouchPromiseScreen.LOGIN
    ) {
        composable(OneTouchPromiseScreen.LOGIN) {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(OneTouchPromiseScreen.SIGNUP) },
                onLoginSuccess = {
                    navController.navigate(OneTouchPromiseScreen.HOME) {
                        popUpTo(OneTouchPromiseScreen.LOGIN) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(OneTouchPromiseScreen.SIGNUP) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(OneTouchPromiseScreen.HOME) {
                        popUpTo(OneTouchPromiseScreen.SIGNUP) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(OneTouchPromiseScreen.HOME) {
            HomeScreen(
                onMeetingClick = { meeting ->
                    navController.navigate("${OneTouchPromiseScreen.MEETING_DETAIL}/${meeting.id}")
                }
            )
        }

        composable(OneTouchPromiseScreen.CREATE_MEETING) {
            CreateMeetingScreen(navController)
        }

        composable(
            route = "${OneTouchPromiseScreen.MEETING_DETAIL}/{meetingId}",
            arguments = listOf(navArgument("meetingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val meetingId = backStackEntry.arguments?.getString("meetingId") ?: ""
            MeetingDetailScreen(navController, meetingId)
        }

        composable(
            route = OneTouchPromiseScreen.MEETING_RESULT,
            arguments = listOf(navArgument("meetingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val meetingId = backStackEntry.arguments?.getString("meetingId") ?: ""
        }

        composable(OneTouchPromiseScreen.MEETING_CONFIRM) {

        }

        composable(OneTouchPromiseScreen.MY_MEETINGS) {

        }

        composable(OneTouchPromiseScreen.SETTINGS) {

        }
    }
}