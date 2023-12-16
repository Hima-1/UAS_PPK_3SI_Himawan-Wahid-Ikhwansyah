package com.himawan.gymstis.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.himawan.gymstis.R
import com.himawan.gymstis.ui.Screen
import com.himawan.gymstis.viewmodel.EditPasswordViewModel

@Composable
fun EditPasswordScreen(
    navController: NavController,
    editPasswordViewModel: EditPasswordViewModel = viewModel(factory = EditPasswordViewModel.Factory)
) {
    var password by remember { mutableStateOf(editPasswordViewModel.newPassword) }
    var passwordVisibility by remember { mutableStateOf(false) }
    val navigateToProfile by editPasswordViewModel.navigateToProfile.collectAsState()

    if (navigateToProfile) {
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
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, contentDescription = "Toggle password visibility")
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                editPasswordViewModel.newPassword = password
                editPasswordViewModel.changePassword()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Update Password")
        }
    }
}