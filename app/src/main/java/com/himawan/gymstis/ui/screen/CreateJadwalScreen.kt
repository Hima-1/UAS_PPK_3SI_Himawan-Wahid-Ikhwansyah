package com.himawan.gymstis.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.himawan.gymstis.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@ExperimentalMaterial3Api
@Composable
fun CreateJadwalScreen(navController: NavController) {
    val context = LocalContext.current
    var selectedDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var quota by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf<Gender?>(Gender.MALE) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.gym_stis_logo),
            contentDescription = "Top Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        DatePickerButton(context, selectedDateMillis) { newDateMillis ->
            selectedDateMillis = newDateMillis
        }

        Spacer(modifier = Modifier.height(12.dp))

        GenderSelection(selectedGender = gender, onGenderSelected = { gender = it })

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = quota,
            onValueChange = { quota = it },
            label = { Text("Kuota") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // TODO: Implement Create Jadwal action
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Create Jadwal")
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun DatePickerButton(context: android.content.Context, selectedDateMillis: Long, onDateSelected: (Long) -> Unit) {
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