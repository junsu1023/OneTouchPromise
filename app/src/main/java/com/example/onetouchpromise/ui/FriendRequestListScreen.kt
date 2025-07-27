package com.example.onetouchpromise.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.model.UserModel
import com.example.onetouchpromise.R
import com.example.onetouchpromise.contract.FriendRequestContract
import com.example.onetouchpromise.util.showToast
import com.example.onetouchpromise.viewmodel.FriendRequestViewModel

@Composable
fun FriendRequestListScreen(
    friendRequestViewModel: FriendRequestViewModel = hiltViewModel()
) {
    val requestList by friendRequestViewModel.friendRequests.collectAsState()
    val state by friendRequestViewModel.friendRequestState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        friendRequestViewModel.fetchFriendRequests()
    }

    LaunchedEffect(state) {
        when(state) {
            is FriendRequestContract.AlreadyFriends -> showToast(context, context.getString(R.string.is_already_friend))
            is FriendRequestContract.AlreadySent -> showToast(context, context.getString(R.string.is_already_request))
            is FriendRequestContract.AlreadyReceived -> showToast(context, context.getString(R.string.is_already_received))
            is FriendRequestContract.Sent -> showToast(context, context.getString(R.string.success_sent))
            else -> { }
        }
    }

    Scaffold(
        containerColor = colorResource(R.color.main_background),
        contentColor = colorResource(R.color.main_background),
        topBar = { FriendRequestTopBar() }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchFriend(
                onSentFriendRequest = { email ->
                    friendRequestViewModel.onFriendRequest(email)
                }
            )

            FriendRequestList(
                requestList = requestList,
                onButtonClick = { (uid, accept) ->
                    friendRequestViewModel.onFriendRequestResponse(
                        fromUid = uid,
                        accept = accept
                    )
                }
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendRequestTopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.main_background)),
        title = {
            Text(
                text = stringResource(R.string.friend_request_list),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.basic_text_color)
                )
            )
        }
    )
}

@Composable
fun SearchFriend(
    onSentFriendRequest: (String) -> Unit
) {
    var addFriendEmail by remember { mutableStateOf("") }

    OutlinedTextField(
        value = addFriendEmail,
        onValueChange = { addFriendEmail = it },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = {
            Text(
                text = stringResource(R.string.add_friend_email),
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
                    if(addFriendEmail.isNotEmpty()) {
                        onSentFriendRequest(addFriendEmail)
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.add_friend_email),
                    tint = if(addFriendEmail.isEmpty()) colorResource(R.color.gray) else colorResource(R.color.basic_icon_color)
                )
            }
        }
    )
}

@Composable
fun FriendRequestList(
    requestList: List<UserModel>,
    onButtonClick: (Pair<String, Boolean>) -> Unit
) {
    LazyColumn {
        items(requestList) { user ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = user.nickname,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.basic_text_color)
                        )
                    )

                    Row {
                        Button(
                            onClick = { onButtonClick(Pair(user.id, true)) },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.accept),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = colorResource(R.color.white),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                        OutlinedButton(
                            onClick = { onButtonClick(Pair(user.id, false)) }
                        ) {
                            Text(
                                text = stringResource(R.string.refusal),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = colorResource(R.color.white),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}