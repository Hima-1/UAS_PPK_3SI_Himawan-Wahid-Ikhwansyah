package com.himawan.gymstis.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.himawan.gymstis.R
import com.himawan.gymstis.ui.component.GenderSelection
import com.himawan.gymstis.ui.viewmodel.EditProfileScreenViewModel
import com.himawan.gymstis.ui.viewmodel.ProfileUpdateResult
import kotlinx.coroutines.launch

enum class Gender { MALE, FEMALE }

@Composable
fun EditProfileScreen(
    navController: NavController,
    editProfileScreenViewModel: EditProfileScreenViewModel = viewModel(factory = EditProfileScreenViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    val name by editProfileScreenViewModel.name.collectAsState()
    val email by editProfileScreenViewModel.email.collectAsState()
    val gender by editProfileScreenViewModel.gender.collectAsState()
    val context = LocalContext.current
    val profileUpdateResult by editProfileScreenViewModel.profileUpdateResult.collectAsState()

    LaunchedEffect(profileUpdateResult) {
        when (profileUpdateResult) {
            ProfileUpdateResult.Success -> {
                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
                editProfileScreenViewModel.resetProfileUpdateResult()
            }

            ProfileUpdateResult.Error -> {
                Toast.makeText(context, "Error updating profile", Toast.LENGTH_SHORT).show()
                editProfileScreenViewModel.resetProfileUpdateResult()
            }

            ProfileUpdateResult.None -> {
                // Do nothing
            }

            ProfileUpdateResult.BadInput -> {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                editProfileScreenViewModel.resetProfileUpdateResult()
            }
        }
    }

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

        OutlinedTextField(
            value = email,
            onValueChange = { editProfileScreenViewModel.updateEmail(it) },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { editProfileScreenViewModel.updateName(it) },
            label = { Text("Name") }
        )
        Spacer(modifier = Modifier.height(12.dp))

        GenderSelection(selectedGender = gender) { selectedGender ->
            editProfileScreenViewModel.updateGender(selectedGender)
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    editProfileScreenViewModel.updateProfile()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Update Profile")
        }
    }
}