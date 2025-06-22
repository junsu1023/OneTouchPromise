package com.example.onetouchpromise.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.error.MeetingDetailError
import com.example.domain.model.MeetingDetailModel
import com.example.domain.model.VoteOptionModel
import com.example.domain.model.VoteType
import com.example.onetouchpromise.R
import com.example.onetouchpromise.viewmodel.MeetingDetailViewModel

@Composable
fun MeetingDetailScreen(
    viewModel: MeetingDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    meetingId: String
) {
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.loadMeetingDetail(meetingId)
    }

    Scaffold(
        topBar = { MeetingDetailTopBar(onBackClick) }
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
fun MeetingDetailTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.meeting_detail)) },
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

@Composable
fun MeetingDetailContent(
    meeting: MeetingDetailModel,
    onVoteClick: (date: VoteOptionModel, location: VoteOptionModel) -> Unit
) {
    var selectedDate by remember { mutableStateOf<VoteOptionModel?>(null) }
    var selectedLocation by remember { mutableStateOf<VoteOptionModel?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.meeting_title),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.participant),
            style = MaterialTheme.typography.titleMedium
        )
        meeting.participants.forEach { participant ->
            Text(text = "• $participant")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.date_vote),
            style = MaterialTheme.typography.titleMedium
        )
        meeting.voteOptions.filter {
            it.type == VoteType.DATE
        }.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedDate == option,
                        onClick = { selectedDate = option }
                    )
                    .padding(4.dp)
            ) {
                RadioButton(
                    selected = selectedLocation == option,
                    onClick = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = "${option.option} (${option.votedUserIds.size}명")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (selectedDate != null && selectedLocation != null) {
                    onVoteClick(selectedDate!!, selectedLocation!!)
                }
            },
            enabled = selectedDate != null && selectedLocation != null,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = stringResource(R.string.progress_vote))
        }
    }
}