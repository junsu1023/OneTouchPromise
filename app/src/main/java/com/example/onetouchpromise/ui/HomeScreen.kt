package com.example.onetouchpromise.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.model.MeetingModel
import com.example.onetouchpromise.R
import com.example.onetouchpromise.component.HomeTabRow
import com.example.onetouchpromise.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onLogoutClick: () -> Unit,
    onWithDraw: () -> Unit,
    onMeetingClick: (Pair<Boolean, MeetingModel>) -> Unit,
    onCreateMeetingClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getCurrentUSer()
    }

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
            ModalDrawerScreen(
                scope = coroutineScope,
                drawerState = drawerState,
                currentUser = uiState.currentUser,
                onLogoutClick = onLogoutClick,
                onWithDraw = onWithDraw
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.background(colorResource(R.color.main_background)),
            containerColor = colorResource(R.color.main_background),
            contentColor = colorResource(R.color.main_background),
            topBar = {
                HomeScreenTopBar(
                    scope = coroutineScope,
                    drawerState = drawerState
                )
            },
            floatingActionButton = {
                if(uiState.meetings.isNotEmpty()) {
                    FloatingActionButton(
                        onClick = onCreateMeetingClick,
                        containerColor = colorResource(R.color.floating_button_color),
                        contentColor = colorResource(R.color.black),
                        shape = CircleShape,
                        elevation = FloatingActionButtonDefaults.elevation(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
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
                    .background(colorResource(R.color.main_background))
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
                        val userNotFoundError = stringResource(R.string.user_not_found)

                        MeetingListView(
                            meetings = uiState.meetings,
                            currentUserEmail = uiState.currentUser?.email ?: "",
                            onMeetingClick = { (isActive, meeting) ->
                                try {
                                    onMeetingClick(Pair(isActive , meeting))
                                } catch (e: NullPointerException) {
                                    viewModel.updateError(userNotFoundError)
                                }
                            }
                        )
                    }
                }
            }
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
        colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.main_background)),
        title = {
            Text(
                text = stringResource(R.string.meeting_schedule),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.basic_text_color)
                )
            )
        },
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
                    contentDescription = stringResource(R.string.menu_open),
                    tint = colorResource(R.color.basic_icon_color)
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
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.basic_text_color2)
            )
        )
    }
}

@Composable
fun HomeErrorMessageView(
    error: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = error,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier.padding(16.dp)
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
            modifier = Modifier.size(80.dp),
            tint = colorResource(R.color.primary)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.is_not_exist_meeting_yet),
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.basic_text_color2)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.click_button_create_meeting),
            style = TextStyle(
                fontSize = 14.sp,
                color = colorResource(R.color.basic_text_color2)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onCreateMeetingClick,
            border = BorderStroke(
                width = 1.dp,
                color = colorResource(R.color.button_container_color)
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colorResource(R.color.button_container_color)
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = stringResource(R.string.create_meeting),
                fontSize = 16.sp
            )
        }
    }
}

enum class MeetingTab {
    ACTIVE,
    CLOSED
}

@Composable
fun MeetingListView(
    meetings: List<MeetingModel>,
    currentUserEmail: String,
    onMeetingClick: (Pair<Boolean, MeetingModel>) -> Unit
) {
    val today by remember { mutableStateOf(LocalDate.now().toString()) }
    var selectedTab by remember { mutableStateOf(MeetingTab.ACTIVE) }
    val filterMeetings = when(selectedTab) {
        MeetingTab.ACTIVE -> meetings.filter { it.voteRatio < 100 && it.dueDate >= today && !it.alreadyVotes.contains(currentUserEmail) }
        MeetingTab.CLOSED -> meetings.filter { it.voteRatio >= 100 || it.dueDate < today || it.alreadyVotes.contains(currentUserEmail) }
    }

    Column {
        HomeTabRow(
            selectedTab = selectedTab,
            tabWidth = 100.dp
        ) {
            MeetingTab.entries.forEachIndexed { index, tab ->
                Tab(
                    modifier = Modifier.background(colorResource(R.color.main_background)),
                    selected = selectedTab.ordinal == index,
                    onClick = { selectedTab = tab },
                    text = {
                        Text(
                            text = if(tab == MeetingTab.ACTIVE) stringResource(R.string.in_progress) else stringResource(R.string.closed),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if(selectedTab.ordinal == index) colorResource(R.color.primary) else colorResource(R.color.basic_text_color2)
                            )
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .background(colorResource(R.color.main_background))
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filterMeetings) { meeting ->
                MeetingCard(
                    meeting = meeting,
                    onClick = { onMeetingClick(Pair(selectedTab == MeetingTab.ACTIVE, meeting)) }
                )
            }
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
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.floating_button_color))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = meeting.title,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.basic_text_color2)
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${stringResource(R.string.due_date)}: ${meeting.dueDate}",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = colorResource(R.color.sub_text_color)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${stringResource(R.string.vote_rates)}: ${(meeting.voteRatio * 100).toInt()}%",
                style = TextStyle(
                    fontSize = 13.sp,
                    color = colorResource(R.color.primary),
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { meeting.voteRatio.coerceIn(0f, 1f) },
                color = colorResource(R.color.primary),
                trackColor = colorResource(R.color.track_color),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
            )
        }
    }
}