package com.himawan.gymstis.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.himawan.gymstis.R

enum class Gender { MALE, FEMALE }

@Composable
fun ChangeProfileScreen(navController: NavController) {
    var email by remember { mutableStateOf("johndoe@example.com") }
    var name by remember { mutableStateOf("John Doe") }
    var gender by remember { mutableStateOf<Gender?>(Gender.MALE) }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

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
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        Spacer(modifier = Modifier.height(12.dp))

        GenderSelection(selectedGender = gender, onGenderSelected = { gender = it })
        Spacer(modifier = Modifier.height(12.dp))

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // TODO: Update user profile
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Update Profile")
        }
    }
}

@Composable
fun GenderSelection(selectedGender: Gender?, onGenderSelected: (Gender) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        GenderButton(
            icon = Icons.Default.Male,
            label = "Male",
            isSelected = selectedGender == Gender.MALE,
            onSelected = { onGenderSelected(Gender.MALE) }
        )
        GenderButton(
            icon = Icons.Default.Female,
            label = "Female",
            isSelected = selectedGender == Gender.FEMALE,
            onSelected = { onGenderSelected(Gender.FEMALE) }
        )
    }
}

@Composable
fun GenderButton(icon: ImageVector, label: String, isSelected: Boolean, onSelected: () -> Unit) {
    OutlinedButton(
        onClick = onSelected,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        )
    ) {
        Icon(imageVector = icon, contentDescription = label)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label)
    }
}
