package com.example.onetouchpromise.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.core.viewmodel.BaseViewModel
import com.example.onetouchpromise.R
import com.example.onetouchpromise.dialog.rememberDatePickerDialog
import com.example.onetouchpromise.navigation.OneTouchPromiseScreen
import com.example.onetouchpromise.util.basePadding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateMeetingScreen(
    navController: NavController
) {
    BackHandler {
        navController.popBackStack()
        BaseViewModel.setCurrentScreen(OneTouchPromiseScreen.HOME)
    }

    val titleState = remember { mutableStateOf("") }
    val dateOptions = remember { mutableStateListOf<LocalDate>() }
    val dueDateState = remember { mutableStateOf<LocalDate?>(null) }

    val datePickerDialog = rememberDatePickerDialog { selectedDate ->
        if(selectedDate != null) dateOptions.add(selectedDate)
    }
    val dueDatePickerDialog = rememberDatePickerDialog { selected ->
        if(selected != null) dueDateState.value = selected
    }

    Column(
        modifier = Modifier
            .basePadding()
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = titleState.value,
            onValueChange = { titleState.value = it },
            label = {
                Text(text = stringResource(R.string.meeting_title))
            },
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.date_candidate),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow {
            dateOptions.forEach { date ->
                AssistChip(
                    onClick = { dateOptions.remove(date) },
                    label = { Text(text = date.format(DateTimeFormatter.ofPattern("MM/dd"))) }
                )
            }

            OutlinedButton(
                onClick = { datePickerDialog.show() }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_date)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = stringResource(R.string.add))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.vote_deadline),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { dueDatePickerDialog.show() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = dueDateState.value?.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) ?: stringResource(R.string.select_date)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                navController.navigate("meeting_detail/1") // 임시 아이디로 넣어둠.
            },
            enabled = titleState.value.isNotBlank() && dateOptions.isNotEmpty() && dueDateState.value != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.complete_create_meeting))
        }
    }
}