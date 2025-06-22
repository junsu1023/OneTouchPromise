package com.example.onetouchpromise.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.error.CreateMeetingError
import com.example.onetouchpromise.R
import com.example.onetouchpromise.viewmodel.CreateMeetingViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.material3.DatePickerDialog as DatePickerDialog1

@Composable
fun CreateMeetingScreen(
    viewModel: CreateMeetingViewModel = hiltViewModel(),
    onMeetingCreated: () -> Unit
) {
    val uiState = viewModel.uiState
    var isDatePickerVisible by remember { mutableStateOf(false) }

    if (isDatePickerVisible) {
        CalendarView(
            setDatePickerVisible = { visible -> isDatePickerVisible = visible },
            addDateOption = { date -> viewModel.addDateOption(date) }
        )
    }

    Scaffold(
        topBar = {
            CreateMeetingTopBar()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            InputMeetingTitleView(
                title = uiState.title,
                onValueChange = { title -> viewModel.updateTitle(title) }
            )

            SelectDueDateView(
                dueDate = uiState.dueDate,
                onDueDateSelected = { dueDate -> viewModel.updateDueDate(dueDate) }
            )

            InputDateView(
                dateOptions = uiState.dateOptions,
                onRemoveDate = { date -> viewModel.removeDateOption(date) },
                setDatePickerVisible = { visible -> isDatePickerVisible = visible }
            )

            InputLocationView(
                locationOptions = uiState.locationOptions,
                onAddLocation = { location -> viewModel.addLocationOption(location) },
                onRemoveLocation = { location -> viewModel.removeLocationOption(location) }
            )

            InputParticipantView(
                participants = uiState.participants,
                onAddParticipant = { participant -> viewModel.addParticipant(participant) },
                onRemoveParticipant = { participatn -> viewModel.removeParticipant(participatn) }
            )


            if (uiState.error != null) {
                ErrorMessageView(uiState.error)
            }

            CreateButtonView(
                onCreateMeeting = { viewModel.createMeeting(onMeetingCreated) },
                isLoading = uiState.isLoading
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMeetingTopBar() {
    TopAppBar(
        title = { Text(text = stringResource(R.string.create_meeting)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarView(
    setDatePickerVisible: (Boolean) -> Unit,
    addDateOption: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog1(
        onDismissRequest = { setDatePickerVisible(false) },
        confirmButton = {
            TextButton(
                onClick = {
                    val millis = datePickerState.selectedDateMillis

                    if (millis != null) {
                        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                            Date(millis)
                        )
                        addDateOption(formattedDate)
                    }

                    setDatePickerVisible(false)
            }) {
                Text(text = stringResource(R.string.add))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { setDatePickerVisible(false) }
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun InputMeetingTitleView(
    title: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = title,
        onValueChange = { onValueChange(it) },
        label = { Text(text = stringResource(R.string.meeting_title)) },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(24.dp))
}

@Composable
fun SelectDueDateView(
    dueDate: String,
    onDueDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = "${year}-${month + 1}-${dayOfMonth}"
                onDueDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp))
    {
        Text(
            text = stringResource(R.string.votes_due_date),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { datePickerDialog.show() }
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = stringResource(R.string.votes_due_date)

            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (dueDate.isNotEmpty()) dueDate else stringResource(R.string.select_date2),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InputDateView(
    dateOptions: List<String>,
    onRemoveDate: (String) -> Unit,
    setDatePickerVisible: (Boolean) -> Unit,
) {
    Text(
        text = stringResource(R.string.date_candidate),
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(Modifier.height(8.dp))

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        dateOptions.forEach { date ->
            ItemChip(
                text = date,
                onDeleteClick = { onRemoveDate(date) }
            )
        }

        AssistChip(
            onClick = { setDatePickerVisible(true) },
            label = { Text(text = stringResource(R.string.add_date)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        )
    }

    Spacer(Modifier.height(24.dp))
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InputLocationView(
    locationOptions: List<String>,
    onAddLocation: (String) -> Unit,
    onRemoveLocation: (String) -> Unit
) {
    var isAddingLocation by remember { mutableStateOf(false) }
    var newLocation by remember { mutableStateOf("") }

    Text(
        text = stringResource(R.string.location_candidate),
        style = MaterialTheme.typography.titleMedium
    )

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        locationOptions.forEach { location ->
            ItemChip(
                text = location,
                onDeleteClick = { onRemoveLocation(location) }
            )
        }

        if (isAddingLocation) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newLocation,
                    onValueChange = { newLocation = it },
                    label = { Text(text = stringResource(R.string.input_location)) },
                    singleLine = true,
                    modifier = Modifier.width(200.dp)
                )

                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = {
                        onAddLocation(newLocation)
                        newLocation = ""
                        isAddingLocation = false
                    }
                ) {
                    Text(text = stringResource(R.string.add))
                }
            }
        } else {
            AssistChip(
                onClick = { isAddingLocation = true },
                label = { Text(text = stringResource(R.string.add_location)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )
                }
            )
        }
    }

    Spacer(Modifier.height(24.dp))
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InputParticipantView(
    participants: List<String>,
    onAddParticipant: (String) -> Unit,
    onRemoveParticipant: (String) -> Unit
) {
    var newParticipant by remember { mutableStateOf("") }

    Text(
        text = stringResource(R.string.add_participant),
        style = MaterialTheme.typography.titleMedium
    )

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = newParticipant,
            onValueChange = { newParticipant = it },
            modifier = Modifier.weight(1f),
            label = { Text(text = stringResource(R.string.input_email)) },
            singleLine = true
        )

        Spacer(Modifier.width(8.dp))

        Button(
            onClick = {
                onAddParticipant(newParticipant)
                newParticipant = ""
            }
        ) {
            Text(text = stringResource(R.string.add))
        }
    }

    FlowRow(
        modifier = Modifier.padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        participants.forEach { participant ->
            ItemChip(
                text = participant,
                onDeleteClick = { onRemoveParticipant(participant) }
            )
        }
    }

    Spacer(Modifier.height(24.dp))
}

@Composable
fun ErrorMessageView(
    error: CreateMeetingError
) {
    val errorText =   when (error) {
        is CreateMeetingError.NotLoggedIn -> stringResource(R.string.need_login)
        is CreateMeetingError.EmptyTitle -> stringResource(R.string.empty_title)
        is CreateMeetingError.NoVoteOptions -> stringResource(R.string.no_vote_options)
        is CreateMeetingError.NoParticipants -> stringResource(R.string.no_participants)
        is CreateMeetingError.DuplicateDateOption -> stringResource(R.string.already_exist_date)
        is CreateMeetingError.DuplicateLocationOption -> stringResource(R.string.already_exist_location)
        is CreateMeetingError.DuplicateParticipantOption -> stringResource(R.string.already_exist_participant)
        is CreateMeetingError.Unknown -> "${stringResource(R.string.unknown)}: ${error.msg}"
    }

    Text(
        text = errorText,
        color = MaterialTheme.colorScheme.error
    )

    Spacer(Modifier.height(12.dp))
}

@Composable
fun CreateButtonView(
    onCreateMeeting: () -> Unit,
    isLoading: Boolean
) {
    Button(
        onClick = { onCreateMeeting() },
        enabled = !isLoading,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp))

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = stringResource(R.string.create_meeting))
        } else {
            Text(text = stringResource(R.string.complete_create_meeting))
        }
    }
}

@Composable
fun ItemChip(
    text: String,
    onDeleteClick: () -> Unit
) {
    AssistChip(
        onClick = { },
        label = { Text(text = text) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.delete),
                modifier = Modifier
                    .size(18.dp)
                    .clickable { onDeleteClick() }
            )
        }
    )
}