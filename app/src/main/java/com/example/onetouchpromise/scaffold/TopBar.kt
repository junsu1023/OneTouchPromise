package com.example.onetouchpromise.scaffold

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onetouchpromise.R
import com.example.onetouchpromise.navigation.OneTouchPromiseScreen
import com.example.onetouchpromise.util.topBarPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    currentScreen: String,
    onBackClick: () -> Unit
) {
    when(currentScreen) {
        OneTouchPromiseScreen.HOME -> {
            TopAppBar(
                modifier = Modifier.topBarPadding(),
                title = {
                    Text(
                        text = stringResource(R.string.home_title),
                        style = TextStyle(
                            fontWeight = FontWeight.Black,
                            fontSize = 30.sp
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
        OneTouchPromiseScreen.CREATE_MEETING -> {
            TopAppBar(
                modifier = Modifier.topBarPadding(),
                title = {
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.clickable { onBackClick() },
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.go_back)
                        )

                        Spacer(modifier = Modifier.width(8.dp) )

                        Text(
                            text = stringResource(R.string.create_meeting),
                            style = TextStyle(
                                fontWeight = FontWeight.Black,
                                fontSize = 30.sp
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
        else -> {
            /*
                TODO
            */
        }
    }
}