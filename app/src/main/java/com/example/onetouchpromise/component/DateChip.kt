package com.example.onetouchpromise.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DateChip(
    date: LocalDate,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if(selected) MaterialTheme.colorScheme.primaryContainer
    else MaterialTheme.colorScheme.surfaceVariant

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Text(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            text = date.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 (E)", Locale.KOREA))
        )
    }
}