package com.example.onetouchpromise.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.onetouchpromise.R
import com.example.onetouchpromise.component.BottomNavigation
import com.example.onetouchpromise.viewmodel.FriendViewModel

@Composable
fun FriendshipScreen(
    curRoute: String?,
    onFriendshipClick: () -> Unit,
    onHomeClick: () -> Unit,
    onSettingClick: () -> Unit,
    onAddFriendClick: () -> Unit,
    friendViewModel: FriendViewModel = hiltViewModel()
) {
    var isSearchMode by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.background(colorResource(R.color.main_background)),
        containerColor = colorResource(R.color.main_background),
        contentColor = colorResource(R.color.main_background),
        topBar = {
            FriendShipScreenTopBar(
                onSearchClick = {
                    isSearchMode = !isSearchMode
                },
                onAddFriendClick = {
                    if(isSearchMode) isSearchMode = false
                    onAddFriendClick()
                }
            )
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if(isSearchMode) {
                SearchFriendBar(
                    onBackClick = { isSearchMode = false }
                )
            }

            FriendList()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendShipScreenTopBar(
    onSearchClick: () -> Unit,
    onAddFriendClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.main_background)),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.friendship),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.basic_text_color)
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search),
                        tint = colorResource(R.color.basic_icon_color),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onSearchClick() }
                    )

                    Icon(
                        imageVector = Icons.Default.PersonAddAlt1,
                        contentDescription = stringResource(R.string.add_friend),
                        tint = colorResource(R.color.basic_icon_color),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onAddFriendClick() }
                    )
                }
            }
        }
    )
}

@Composable
fun SearchFriendBar(
    onBackClick: () -> Unit
) {
    BackHandler { onBackClick() }

    var searchNickName by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchNickName,
        onValueChange = { searchNickName = it },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = stringResource(R.string.search),
                style = TextStyle(
                    color = colorResource(R.color.gray),
                    fontSize = 16.sp
                )
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(R.color.outlined_focused_border),
            unfocusedBorderColor = colorResource(R.color.outlined_unfocused_border),
            focusedTextColor = colorResource(R.color.basic_text_color),
            unfocusedTextColor = colorResource(R.color.basic_text_color)
        ),
        trailingIcon = {
            IconButton(
                onClick = {
                    if(searchNickName.isNotEmpty()) {
                        /*
                        * TODO
                        * 검색 시 포홤 혹은 일치하는 친구 목록 가져오기
                        */
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search),
                    tint = if(searchNickName.isEmpty()) colorResource(R.color.gray) else colorResource(R.color.basic_icon_color)
                )
            }
        }
    )
}

@Composable
fun FriendList() {
    var isExpanded by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.clickable { isExpanded = !isExpanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.friendship),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Thin,
                    color = colorResource(R.color.gray)
                )
            )

            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                imageVector = if(isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = stringResource(R.string.expanded_status),
                modifier = Modifier.size(24.dp)
            )
        }

        if(isExpanded) {
            /*
            TODO
            친구 목록 가져오기
             */
        }
    }
}