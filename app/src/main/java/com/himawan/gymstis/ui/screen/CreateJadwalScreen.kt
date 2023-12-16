package com.himawan.gymstis.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.himawan.gymstis.R
import com.himawan.gymstis.ui.Screen
import com.himawan.gymstis.ui.component.DatePickerButton
import com.himawan.gymstis.ui.component.GenderSelection
import com.himawan.gymstis.viewmodel.CreateJadwalViewModel
import java.time.Instant

import java.time.ZoneId

@ExperimentalMaterial3Api
@Composable
fun CreateJadwalScreen(
    navController: NavController,
    createJadwalViewModel: CreateJadwalViewModel = viewModel(factory = CreateJadwalViewModel.Factory)
) {
    val context = LocalContext.current
    val navigateToJadwal by createJadwalViewModel.navigateToJadwal.collectAsState()

    if (navigateToJadwal) {
        navController.navigate(Screen.Jadwal.route)
    }

    var selectedDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var gender by remember { mutableStateOf(createJadwalViewModel.gender) }
    var kuota by remember { mutableStateOf(createJadwalViewModel.kuota) }
    
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

        var selectedDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }
        DatePickerButton(context, selectedDateMillis) { newDateMillis ->
            val newDate =
                Instant.ofEpochMilli(newDateMillis).atZone(ZoneId.systemDefault()).toLocalDate()
            createJadwalViewModel.updateDate(newDate)
        }

        Spacer(modifier = Modifier.height(12.dp))

        GenderSelection(selectedGender = gender) { newGender ->
            gender = newGender
            createJadwalViewModel.updateGender(newGender)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = kuota,
            onValueChange = {
                kuota = it
                createJadwalViewModel.updateKuota(it)
            },
            label = { Text("Kuota") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                createJadwalViewModel.createJadwal()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Create Jadwal")
        }
    }
}
