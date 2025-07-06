package com.example.onetouchpromise.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.error.MeetingDetailError
import com.example.domain.model.MeetingResultModel
import com.example.domain.model.VoteResultItem
import com.example.onetouchpromise.R
import com.example.onetouchpromise.viewmodel.MeetingResultViewModel

@Composable
fun MeetingResultScreen(
    meetingResultViewModel: MeetingResultViewModel = hiltViewModel(),
    meetingId: String,
    onBackClick: () -> Unit
) {
    val uiState = meetingResultViewModel.uiState

    LaunchedEffect(Unit) {
        meetingResultViewModel.loadMeetingResult(meetingId)
    }

    Scaffold(
        topBar = {
            ResultScreenTopBar(onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    ResultLoadingView(modifier = Modifier.align(Alignment.Center))
                }
                uiState.error != null -> {
                    ResultErrorMessageView(
                        error = uiState.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.meeting != null -> {
                    ResultContent(meeting = uiState.meeting)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreenTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.voted_result)) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.go_back)
                )
            }
        }
    )
}

@Composable
fun ResultLoadingView(modifier: Modifier) {
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
fun ResultErrorMessageView(
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
        modifier = modifier.padding(16.dp)
    )
}

@Composable
fun ResultContent(
    meeting: MeetingResultModel,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = meeting.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        item {
            VoteResultSection(
                title = stringResource(R.string.date_vote_result),
                results = meeting.dateResults
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            VoteResultSection(
                title = stringResource(R.string.location_vote_result),
                results = meeting.locationResults
            )
        }
    }
}

@Composable
fun VoteResultSection(
    title: String,
    results: List<VoteResultItem>
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if(results.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_participant),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            val maxVotes = results.maxOf { it.voteCount }.coerceAtLeast(1)
            results.forEach { item ->
                VoteResultBar(item = item, maxVotes = maxVotes)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun VoteResultBar(
    item: VoteResultItem,
    maxVotes: Int
) {
    Column {
        Text(
            text = "${item.option} (${item.voteCount}표)",
            style = MaterialTheme.typography.bodyMedium
        )

        val progressRatio = item.voteCount.toFloat() / maxVotes

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color = colorResource(R.color.pale_indigo_blue))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progressRatio)
                    .background(color = colorResource(R.color.deep_indigo_blue))
            )
        }
    }
}