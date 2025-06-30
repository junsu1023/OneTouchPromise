package com.example.onetouchpromise.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.error.MeetingDetailError
import com.example.domain.model.MeetingDetailModel
import com.example.onetouchpromise.R
import com.example.onetouchpromise.viewmodel.MeetingDetailViewModel

@Composable
fun MeetingDetailScreen(
    viewModel: MeetingDetailViewModel = hiltViewModel(),
    meetingId: String,
    onBackClick: () -> Unit = {},
    onVoteSuccess: () -> Unit
) {
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.loadMeetingDetail(meetingId)
    }

    LaunchedEffect(uiState.isVoteSuccess) {
        if(uiState.isVoteSuccess) {
            onVoteSuccess()
        }
    }

    Scaffold(
        topBar = {
            MeetingDetailTopBar(
                title = uiState.meeting?.title ?: "",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    DetailLoadingView(modifier = Modifier.align(Alignment.Center))
                }
                uiState.error != null -> {
                    DetailErrorMessageView(
                        error = uiState.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.meeting != null -> {
                    MeetingDetailContent(
                        meeting = uiState.meeting,
                        onVoteClick = { date, location ->
                            viewModel.submitVote(meetingId, date, location)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingDetailTopBar(
    title: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.go_back)
                )
            }
        }
    )
}

@Composable
fun DetailLoadingView(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.getting_meeting_detail),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DetailErrorMessageView(
    error: MeetingDetailError,
    modifier: Modifier = Modifier
) {
    val message = when(error) {
        is MeetingDetailError.NotLoggedIn -> stringResource(R.string.need_login)
        is MeetingDetailError.NotFound -> stringResource(R.string.meeting_not_found)
        is MeetingDetailError.UnKnown -> stringResource(R.string.unknown)
        else -> stringResource(R.string.occur_error)
    }

    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeetingDetailContent(
    meeting: MeetingDetailModel,
    onVoteClick: (date: String, location: String) -> Unit
) {
    var selectedDate by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "${stringResource(R.string.due_date)}: ${meeting.dueDate}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,

            ) {
                Text(
                    text = stringResource(R.string.participant),
                    style = MaterialTheme.typography.titleMedium
                )

                Box(
                    modifier = Modifier.clickable {
                        isExpanded = !isExpanded
                    }
                ) {
                    Icon(
                        imageVector = if(isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.expanded_status),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            if(isExpanded) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    meeting.participants.forEach {
                        AssistChip(
                            onClick = { },
                            label = { Text(text = it) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.date_vote),
            style = MaterialTheme.typography.titleMedium
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            meeting.dateOptions.forEach { date ->
                FilterChip(
                    selected = selectedDate == date,
                    onClick = { selectedDate = date },
                    label = { Text(text = date) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.location_vote),
            style = MaterialTheme.typography.titleMedium
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            meeting.locationOptions.forEach { location ->
                FilterChip(
                    selected = selectedLocation == location,
                    onClick = { selectedLocation = location },
                    label = { Text(text = location) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (selectedDate.isNotEmpty() && selectedLocation.isNotEmpty()) {
                    onVoteClick(selectedDate, selectedLocation)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedDate.isNotEmpty() && selectedLocation.isNotEmpty()
        ) {
            Text(text = stringResource(R.string.progress_vote))
        }
    }
}