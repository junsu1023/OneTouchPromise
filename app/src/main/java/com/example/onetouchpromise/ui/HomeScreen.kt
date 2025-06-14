package com.example.onetouchpromise.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.onetouchpromise.R
import com.example.onetouchpromise.test.Meeting

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val mockMeetings = remember {
        listOf(
            Meeting("1", "test1", "2025-06-14", 5, 10),
            Meeting("2", "test2", "2025-06-15", 6, 10),
            Meeting("3", "test3", "2025-06-16", 7, 10),
            Meeting("4", "test3", "2025-06-16", 8, 10)
        )
    }

    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        items(mockMeetings) { meeting ->
            MeetingCard(
                meeting = meeting,
                onClick = {
                    navController.navigate("meeting_detail/${meeting.id}")
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun MeetingCard(
    meeting: Meeting,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = meeting.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${stringResource(R.string.votes_due_date)} ${meeting.dueDate}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = { meeting.votedCount.toFloat() / meeting.totalCount },
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(R.color.dark_slate_gary),
                trackColor = colorResource(R.color.philippine_silver)
            )

            Text(
                text = "${meeting.votedCount}/${meeting.totalCount}명 투표 완료",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}