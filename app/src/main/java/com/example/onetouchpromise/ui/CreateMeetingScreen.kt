package com.example.onetouchpromise.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.error.CreateMeetingError
import com.example.onetouchpromise.R
import com.example.onetouchpromise.viewmodel.CreateMeetingViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun CreateMeetingScreen(
    viewModel: CreateMeetingViewModel = hiltViewModel(),
    onMeetingCreated: () -> Unit
) {
    val uiState = viewModel.uiState

    Scaffold(
        containerColor = colorResource(R.color.main_background),
        contentColor = colorResource(R.color.main_background),
        topBar = {
            CreateMeetingTopBar()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .background(colorResource(R.color.main_background))
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
                onDateSelected = { date -> viewModel.addDateOption(date) }
            )

            InputLocationView(
                locationOptions = uiState.locationOptions,
                onAddLocation = { location -> viewModel.addLocationOption(location) },
                onRemoveLocation = { location -> viewModel.removeLocationOption(location) }
            )

            InputParticipantView(
                participants = uiState.participants,
                onAddParticipant = { participant -> viewModel.addParticipant(participant) },
                onRemoveParticipant = { participant -> viewModel.removeParticipant(participant) }
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
        colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.main_background)),
        title = {
            Text(
                text = stringResource(R.string.create_meeting),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.basic_text_color)
                )
            )
        }
    )
}

@Composable
fun InputMeetingTitleView(
    title: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = title,
        onValueChange = { onValueChange(it) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(R.string.meeting_title),
                style = TextStyle(
                    color = colorResource(R.color.gray),
                    fontSize = 16.sp
                )
            )
        },
        trailingIcon = {
            if(title.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.delete_all),
                    tint = colorResource(R.color.black),
                    modifier = Modifier.clickable {
                        onValueChange("")
                    }
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(R.color.outlined_focused_border),
            unfocusedBorderColor = colorResource(R.color.outlined_unfocused_border),
            focusedTextColor = colorResource(R.color.basic_text_color),
            unfocusedTextColor = colorResource(R.color.basic_text_color)
        )
    )

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun SelectDueDateView(
    dueDate: String,
    onDueDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val date = LocalDate.parse("%02d-%02d-%02d".format(year, month + 1, dayOfMonth), formatter)

                onDueDateSelected(date)
            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
    }

    Text(
        text = stringResource(R.string.votes_due_date),
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            color = colorResource(R.color.basic_text_color)
        )
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { datePickerDialog.show() }
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = stringResource(R.string.votes_due_date),
            tint = colorResource(R.color.gray)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = dueDate.ifEmpty { stringResource(R.string.select_date2) },
            style = MaterialTheme.typography.bodyLarge,
            color = if(dueDate.isEmpty()) colorResource(R.color.gray) else colorResource(R.color.basic_text_color)
        )
    }

    Spacer(modifier = Modifier.height(24.dp))
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InputDateView(
    dateOptions: List<String>,
    onRemoveDate: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val date = LocalDate.parse("%02d-%02d-%02d".format(year, month + 1, dayOfMonth), formatter)
                onDateSelected(date)
            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.date_candidate),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = colorResource(R.color.basic_text_color)
            )
        )

        if(dateOptions.isEmpty()) {
            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clickable { expanded.value = !expanded.value }
            ) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = stringResource(R.string.add_date_candidate),
                    tint = colorResource(R.color.basic_icon_color)
                )
            }
        }
    }

    if(expanded.value) {
        Spacer(Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            dateOptions.forEach { date ->
                ItemChip(
                    text = date,
                    onDeleteClick = { onRemoveDate(date) }
                )
            }

            AssistChip(
                onClick = { datePickerDialog.show() },
                label = {
                    Text(
                        text = stringResource(R.string.add_date),
                        color = colorResource(R.color.basic_text_color)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            )
        }
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
    val expanded = remember { mutableStateOf(false) }
    var newLocation by remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.location_candidate),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = colorResource(R.color.basic_text_color)
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        if(locationOptions.isEmpty()) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clickable { expanded.value = !expanded.value }
            ) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = stringResource(R.string.add_location_candidate),
                    tint = colorResource(R.color.basic_icon_color)
                )
            }
        }
    }

    if(expanded.value) {
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newLocation,
            onValueChange = { newLocation = it },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(R.string.input_location),
                    style = TextStyle(
                        color = colorResource(R.color.gray),
                        fontSize = 16.sp
                    )
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = stringResource(R.string.location)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.outlined_focused_border),
                unfocusedBorderColor = colorResource(R.color.outlined_unfocused_border),
                focusedTextColor = colorResource(R.color.basic_text_color),
                unfocusedTextColor = colorResource(R.color.basic_text_color)
            ),
            trailingIcon = {
                IconButton(
                    onClick = {
                        if(newLocation.isNotEmpty()) {
                            onAddLocation(newLocation)
                            newLocation = ""
                        }
                    },
                ) {
                    Text(
                        text = stringResource(R.string.add),
                        color = if(newLocation.isEmpty()) colorResource(R.color.gray) else colorResource(R.color.basic_text_color)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            locationOptions.forEach { location ->
                ItemChip(
                    text = location,
                    onDeleteClick = { onRemoveLocation(location) }
                )
            }
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
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            color = colorResource(R.color.basic_text_color)
        )
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = newParticipant,
        onValueChange = { newParticipant = it },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = stringResource(R.string.input_email),
                style = TextStyle(
                    color = colorResource(R.color.gray),
                    fontSize = 16.sp
                )
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(R.color.outlined_focused_border),
            unfocusedBorderColor = colorResource(R.color.outlined_unfocused_border),
            focusedTextColor = colorResource(R.color.basic_text_color),
            unfocusedTextColor = colorResource(R.color.basic_text_color)
        ),
        trailingIcon = {
            IconButton(
                onClick = {
                    if(newParticipant.isNotEmpty()) {
                        onAddParticipant(newParticipant)
                        newParticipant = ""
                    }
                },
            ) {
                Text(
                    text = stringResource(R.string.add),
                    color = if(newParticipant.isEmpty()) colorResource(R.color.gray) else colorResource(R.color.basic_text_color)
                )
            }
        }
    )

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
fun CreateButtonView(
    onCreateMeeting: () -> Unit,
    isLoading: Boolean
) {
    Button(
        onClick = { onCreateMeeting() },
        enabled = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.button_container_color),
            contentColor = colorResource(R.color.white)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp))

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(R.string.create_meeting),
                fontWeight = FontWeight.Bold
            )
        } else {
            Text(
                text = stringResource(R.string.complete_create_meeting),
                fontWeight = FontWeight.Bold
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))
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

@Composable
fun ErrorMessageView(
    error: CreateMeetingError
) {
    val errorText =   when (error) {
        is CreateMeetingError.NotLoggedIn -> stringResource(R.string.need_login)
        is CreateMeetingError.EmptyTitle -> stringResource(R.string.empty_title)
        is CreateMeetingError.EmptyDueDate -> stringResource(R.string.empty_duedate)
        is CreateMeetingError.NoVoteOptions -> stringResource(R.string.no_vote_options)
        is CreateMeetingError.NoParticipants -> stringResource(R.string.no_participants)
        is CreateMeetingError.DuplicateDateOption -> stringResource(R.string.already_exist_date)
        is CreateMeetingError.DuplicateLocationOption -> stringResource(R.string.already_exist_location)
        is CreateMeetingError.DuplicateParticipantOption -> stringResource(R.string.already_exist_participant)
        is CreateMeetingError.NotAfterDate -> stringResource(R.string.is_not_after_date)
        is CreateMeetingError.Unknown -> "${stringResource(R.string.unknown)}: ${error.msg}"
    }

    Text(
        text = errorText,
        color = MaterialTheme.colorScheme.error
    )

    Spacer(Modifier.height(12.dp))
}