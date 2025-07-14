package com.example.onetouchpromise.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.UserModel
import com.example.onetouchpromise.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ModalDrawerScreen(
    scope: CoroutineScope,
    drawerState: DrawerState,
    currentUser: UserModel?,
    onLogoutClick: () -> Unit,
    onWithDraw: () -> Unit
) {
    ModalDrawerSheet {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.go_back),
                    tint = colorResource(R.color.basic_icon_color)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(R.string.setting),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.basic_text_color)
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.padding(start = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(
                text = stringResource(R.string.logout),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.basic_text_color2)
                ),
                modifier = Modifier.clickable {
                    scope.launch {
                        drawerState.close()
                    }
                    onLogoutClick()
                }
            )

            Text(
                text = stringResource(R.string.withdraw),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.basic_text_color2)
                ),
                modifier = Modifier.clickable {
                    scope.launch {
                        drawerState.close()
                    }
                    onWithDraw()
                }
            )
        }
    }
}