package com.example.onetouchpromise.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
        modifier = Modifier.background(colorResource(R.color.main_background)),
        containerColor = colorResource(R.color.main_background),
        contentColor = colorResource(R.color.main_background),
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
    TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.main_background)),
        title = {
            Text(
                text = stringResource(R.string.voted_result),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.basic_text_color)
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.go_back),
                    tint = colorResource(R.color.basic_icon_color)
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
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.basic_text_color2)
            )
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
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(R.color.basic_text_color2)
        ),
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
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.basic_text_color2)
                ),
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
    val maxVotes = results.maxOf { it.voteCount }.coerceAtLeast(1)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.result_card_color))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.basic_text_color2)
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            results.forEach { item ->
                VoteResultBar(item = item, maxVotes = maxVotes)
            }
        }
    }
}

@Composable
fun VoteResultBar(
    item: VoteResultItem,
    maxVotes: Int
) {
    Text(
        text = "${item.option} (${item.voteCount}표)",
        style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(R.color.basic_text_color2)
        )
    )

    Spacer(modifier = Modifier.height(6.dp))

    LinearProgressIndicator(
        progress = { item.voteCount.toFloat() / maxVotes },
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(RoundedCornerShape(12.dp)),
        color = colorResource(R.color.progress_indicator_bar_color),
        trackColor = colorResource(R.color.progress_indicator_track_color)
    )

    Spacer(modifier = Modifier.height(16.dp))
}