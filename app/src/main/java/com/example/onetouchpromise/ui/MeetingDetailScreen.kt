package com.example.onetouchpromise.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.core.viewmodel.BaseViewModel
import com.example.onetouchpromise.R
import com.example.onetouchpromise.component.DateChip
import com.example.onetouchpromise.navigation.OneTouchPromiseScreen
import com.example.onetouchpromise.test.Meeting
import com.example.onetouchpromise.util.basePadding
import java.time.LocalDate

@Composable
fun MeetingDetailScreen(
    navController: NavHostController,
    meetingId: String
) {
    BackHandler {
        navController.popBackStack()
        BaseViewModel.setCurrentScreen(OneTouchPromiseScreen.HOME)
    }

    val meeting = remember {
        Meeting(
            id = meetingId,
            title = "임시 제목",
            dueDate = "2025-06-20",
            votedCount = 3,
            totalCount = 6
        )
    }

    val dateOptions = remember {
        listOf(
            LocalDate.of(2025, 6,  21),
            LocalDate.of(2025, 6,  22),
            LocalDate.of(2025, 6,  23)
        )
    }

    val selectedDates = remember { mutableStateListOf<LocalDate>() }

    Column(
        modifier = Modifier
            .basePadding()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = meeting.title,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        )

        Text(
            text = "${stringResource(R.string.votes_due_date)} ${meeting.dueDate}"
        )

        Text(
            text = "${meeting.votedCount}/${meeting.totalCount}${stringResource(R.string.how_many_votes_complete)}"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.select_possible_date),
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            dateOptions.forEach { date ->
                val selected = selectedDates.contains(date)

                DateChip(
                    date = date,
                    selected = selected,
                    onClick =  {
                        if(selected) selectedDates.remove(date)
                        else selectedDates.add(date)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                /* 투표 저장 */
                navController.navigate("meeting_result/$meetingId")
            },
            enabled = selectedDates.isNotEmpty()
        ) {
            Text(
                text = stringResource(R.string.submit_vote)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate("meeting_reuslt/$meetingId")
            }
        ) {
            Text(
                text = stringResource(R.string.view_voting_status)
            )
        }
    }
}