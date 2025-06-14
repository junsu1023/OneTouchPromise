package com.example.onetouchpromise.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.onetouchpromise.ui.HomeScreen

@Composable
fun OneTouchPromiseNavHost(
    navController: NavHostController
) {
    val viewModelHostOwner = checkNotNull(LocalViewModelStoreOwner.current)

    NavHost(
        navController = navController,
        startDestination = OneTouchPromiseScreen.HOME
    ) {
        composable(OneTouchPromiseScreen.HOME) {
            HomeScreen(navController)
        }

        composable(OneTouchPromiseScreen.CREATE_MEETING) {

        }

        composable(
            route = OneTouchPromiseScreen.MEETING_DETAIL,
            arguments = listOf(navArgument("meetingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val meetingId = backStackEntry.arguments?.getString("meetingId") ?: ""
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