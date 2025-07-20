package com.example.onetouchpromise.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.onetouchpromise.R
import com.example.onetouchpromise.navigation.OneTouchPromiseScreen

@Composable
fun BottomNavigation(
    curRoute: String?,
    onFriendshipClick: () -> Unit,
    onHomeClick: () -> Unit,
    onSettingClick: () -> Unit
) {
    val items = listOf(
        ScreenInfo(OneTouchPromiseScreen.FRIENDSHIP, Icons.Default.Person3, "friendship") to onFriendshipClick,
        ScreenInfo(OneTouchPromiseScreen.HOME, Icons.Default.Home, "home") to onHomeClick,
        ScreenInfo(OneTouchPromiseScreen.SETTING, Icons.Default.Settings, "setting") to onSettingClick
    )

    AnimatedVisibility(
        visible = items.map { it.first.route }.contains(curRoute)
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            items.forEach { (screen, onClick) ->
                NavigationBarItem(
                    selected = curRoute == screen.route,
                    icon = {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.label,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorResource(R.color.deep_indigo_blue),
                        unselectedIconColor = colorResource(R.color.gray),
                        indicatorColor = Color.Transparent
                    ),
                    onClick = onClick
                )
            }
        }
    }
}

data class ScreenInfo(
    val route: String,
    val icon: ImageVector,
    val label: String
)