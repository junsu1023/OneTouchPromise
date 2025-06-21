package com.example.onetouchpromise.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.error.MeetingError
import com.example.domain.model.MeetingModel
import com.example.onetouchpromise.R
import com.example.onetouchpromise.util.basePadding
import com.example.onetouchpromise.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onLogoutClick: () -> Unit,
    onMeetingClick: (MeetingModel) -> Unit,
    onCreateMeetingClick: () -> Unit
) {
    val uiState = viewModel.uiState
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    BackHandler(
        enabled = drawerState.isOpen
    ) {
        coroutineScope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                scope = coroutineScope,
                drawerState = drawerState,
                onLogoutClick = onLogoutClick
            )
        }
    ) {
        Scaffold(
            topBar = {
                HomeScreenTopBar(
                    scope = coroutineScope,
                    drawerState = drawerState
                )
            },
            floatingActionButton = {
                if(uiState.meetings.isNotEmpty()) {
                    FloatingActionButton(
                        onClick = onCreateMeetingClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.create_meeting)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    uiState.isLoading -> {
                        HomeLoadingView(modifier = Modifier.align(Alignment.Center))
                    }
                    uiState.error != null -> {
                        HomeErrorMessageView(
                            error = uiState.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    uiState.meetings.isEmpty() -> {
                        EmptyListView(
                            modifier = Modifier.align(Alignment.Center),
                            onCreateMeetingClick = onCreateMeetingClick
                        )
                    }
                    else -> {
                        MeetingListView(
                            meetings = uiState.meetings,
                            onMeetingClick = { meeting -> onMeetingClick(meeting) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModalDrawerSheet(
    scope: CoroutineScope,
    drawerState: DrawerState,
    onLogoutClick: () -> Unit
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
                    contentDescription = stringResource(R.string.go_back)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(R.string.setting),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                scope.launch {
                    drawerState.close()
                }
                onLogoutClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = stringResource(R.string.logout))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.meeting_schedule)) },
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.menu_open)
                )
            }
        }
    )
}

@Composable
fun HomeLoadingView(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.getting_meeting_list),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun HomeErrorMessageView(
    error: MeetingError,
    modifier: Modifier = Modifier
) {
    val message = when(error) {
        is MeetingError.UserNotLoggedIn -> stringResource(R.string.use_after_login)
        is MeetingError.NetworkError -> "${stringResource(R.string.network_error)}: ${error.message}"
        is MeetingError.Unknown -> stringResource(R.string.unknown)
    }

    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}

@Composable
fun EmptyListView(
    modifier: Modifier,
    onCreateMeetingClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = stringResource(R.string.is_blank_meeting),
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.is_not_exist_meeting_yet),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.click_button_create_meeting),
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onCreateMeetingClick
        ) {
            Text(text = stringResource(R.string.create_meeting))
        }
    }
}

@Composable
fun MeetingListView(
    meetings: List<MeetingModel>,
    onMeetingClick: (MeetingModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .basePadding()
            .background(Color.White)
    ) {
        items(meetings) { meeting ->
            MeetingCard(
                meeting = meeting,
                onClick = { onMeetingClick(meeting) }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun MeetingCard(
    meeting: MeetingModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = meeting.title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "${stringResource(R.string.date)}: ${meeting.date}")
            Text(text = "${stringResource(R.string.participant_count)}: ${meeting.participantCount}명")
        }
    }
}