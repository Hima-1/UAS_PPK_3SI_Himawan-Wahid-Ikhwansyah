package com.himawan.gymstis.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.himawan.gymstis.R
import com.himawan.gymstis.ui.Screen
import com.himawan.gymstis.ui.component.GenderSelection
import com.himawan.gymstis.viewmodel.EditProfileScreenViewModel
import com.himawan.gymstis.viewmodel.LoginViewModel
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
    val profileUpdated by editProfileScreenViewModel.profileUpdated.collectAsState()

    if (profileUpdated) {
        navController.navigate(Screen.User.route)
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