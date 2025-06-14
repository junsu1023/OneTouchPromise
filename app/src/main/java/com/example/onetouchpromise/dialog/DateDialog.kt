package com.example.onetouchpromise.dialog

import android.app.DatePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate

@Composable
fun rememberDatePickerDialog(
    onDateSelected: (LocalDate?) -> Unit
): DatePickerDialog {
    val context = LocalContext.current
    val today = LocalDate.now()

    return remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
            },
            today.year,
            today.monthValue - 1,
            today.dayOfMonth
        )
    }
}