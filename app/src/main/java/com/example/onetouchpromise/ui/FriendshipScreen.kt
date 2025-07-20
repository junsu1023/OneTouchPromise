package com.example.onetouchpromise.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.onetouchpromise.R
import com.example.onetouchpromise.component.BottomNavigation

@Composable
fun FriendshipScreen(
    curRoute: String?,
    onFriendshipClick: () -> Unit,
    onHomeClick: () -> Unit,
    onSettingClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.background(colorResource(R.color.main_background)),
        containerColor = colorResource(R.color.main_background),
        contentColor = colorResource(R.color.main_background),
        topBar = {
            FriendShipScreenTopBar()
        },
        bottomBar = {
            BottomNavigation(
                curRoute = curRoute,
                onFriendshipClick = onFriendshipClick,
                onHomeClick = onHomeClick,
                onSettingClick = onSettingClick
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendShipScreenTopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.main_background)),
        title = {
            Text(
                text = stringResource(R.string.friendship),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.basic_text_color)
                )
            )
        }
    )
}