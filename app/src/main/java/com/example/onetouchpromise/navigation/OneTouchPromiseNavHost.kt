package com.example.onetouchpromise.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.onetouchpromise.R
import com.example.onetouchpromise.ui.CreateMeetingScreen
import com.example.onetouchpromise.ui.FriendshipScreen
import com.example.onetouchpromise.ui.HomeScreen
import com.example.onetouchpromise.ui.LoginScreen
import com.example.onetouchpromise.ui.MeetingDetailScreen
import com.example.onetouchpromise.ui.MeetingResultScreen
import com.example.onetouchpromise.ui.SettingScreen
import com.example.onetouchpromise.ui.SignUpScreen
import com.example.onetouchpromise.ui.SplashScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OneTouchPromiseNavHost(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val curRoute = navBackStackEntry?.destination?.route

    val onFriendshipClick: () -> Unit = {
        navController.navigate(OneTouchPromiseScreen.FRIENDSHIP) {
            popUpTo(0) {
                inclusive = false
            }
        }
    }

    val onHomeClick: () -> Unit = {
        navController.navigate(OneTouchPromiseScreen.HOME) {
            popUpTo(0) {
                inclusive = false
            }
        }
    }

    val onSettingClick: () -> Unit = {
        navController.navigate(OneTouchPromiseScreen.SETTING) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = OneTouchPromiseScreen.SPLASH
    ) {
        composable(OneTouchPromiseScreen.SPLASH) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(OneTouchPromiseScreen.HOME) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(OneTouchPromiseScreen.LOGIN) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

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
                curRoute = curRoute,
                onMeetingClick = { (isActive, meeting) ->
                    if(isActive) {
                        navController.navigate("${OneTouchPromiseScreen.MEETING_DETAIL}/${meeting.id}")
                    } else {
                        navController.navigate("${OneTouchPromiseScreen.MEETING_RESULT}/${meeting.id}")
                    }
                },
                onCreateMeetingClick = {
                    navController.navigate(OneTouchPromiseScreen.CREATE_MEETING)
                },
                onFriendshipClick = onFriendshipClick,
                onHomeClick = onHomeClick,
                onSettingClick = onSettingClick
            )
        }

        composable(OneTouchPromiseScreen.CREATE_MEETING) {
            CreateMeetingScreen(
                onMeetingCreated = {
                    navController.navigate(OneTouchPromiseScreen.HOME) {
                        popUpTo(OneTouchPromiseScreen.HOME) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = "${OneTouchPromiseScreen.MEETING_DETAIL}/{meetingId}",
            arguments = listOf(navArgument("meetingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val meetingId = backStackEntry.arguments?.getString("meetingId") ?: ""
            
            MeetingDetailScreen(
                meetingId = meetingId,
                onBackClick = { navController.popBackStack() },
                onVoteSuccess = {
                    navController.navigate(OneTouchPromiseScreen.HOME) {
                        popUpTo(OneTouchPromiseScreen.HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "${OneTouchPromiseScreen.MEETING_RESULT}/{meetingId}",
            arguments = listOf(navArgument("meetingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val meetingId = backStackEntry.arguments?.getString("meetingId") ?: ""

            MeetingResultScreen(
                meetingId = meetingId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(OneTouchPromiseScreen.SETTING) {
            val context = LocalContext.current
            val successWithDraw = stringResource(R.string.withdraw_success)
            val failedWithDraw = stringResource(R.string.withdraw_failed)

            SettingScreen(
                curRoute = curRoute,
                onLogout = {
                    FirebaseAuth.getInstance().signOut()

                    navController.navigate(OneTouchPromiseScreen.LOGIN) {
                        popUpTo(OneTouchPromiseScreen.HOME) {
                            inclusive = true
                        }
                    }
                },
                onWithDraw = {
                    val auth = FirebaseAuth.getInstance()

                    auth.currentUser?.delete()?.addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            Toast.makeText(context, successWithDraw, Toast.LENGTH_SHORT).show()

                            navController.navigate(OneTouchPromiseScreen.LOGIN) {
                                popUpTo(OneTouchPromiseScreen.HOME) {
                                    inclusive = true
                                }
                            }
                        } else {
                            Toast.makeText(context, failedWithDraw, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onFriendshipClick = onFriendshipClick,
                onHomeClick = onHomeClick,
                onSettingClick = onSettingClick
            )
        }

        composable(OneTouchPromiseScreen.FRIENDSHIP) {
            FriendshipScreen(
                curRoute = curRoute,
                onFriendshipClick = onFriendshipClick,
                onHomeClick = onHomeClick,
                onSettingClick = onSettingClick
            )
        }
    }
}