package com.himawan.gymstis.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@ExperimentalMaterial3Api
@Composable
fun DatePickerButton(
    context: android.content.Context,
    selectedDateMillis: Long,
    onDateSelected: (Long) -> Unit
) {
    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val selectedDate = dateFormatter.format(Date(selectedDateMillis))
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = selectedDateMillis
        }
        android.app.DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(year, month, day)
                onDateSelected(calendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
        showDialog = false
    }

    OutlinedButton(onClick = { showDialog = true }) {
        Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Select Date")
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = selectedDate)
    }
}