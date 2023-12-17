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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.himawan.gymstis.R
import com.himawan.gymstis.ui.component.GenderSelection
import com.himawan.gymstis.ui.viewmodel.RegisterResult
import com.himawan.gymstis.ui.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navController: NavHostController,
    registerViewModel: RegisterViewModel = viewModel(factory = RegisterViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val email by registerViewModel.email
    val name by registerViewModel.name
    val password by registerViewModel.password
    val gender by registerViewModel.gender
    val passwordVisibility by registerViewModel.passwordVisibility
    val registerResult by registerViewModel.registerResult.collectAsState()

    LaunchedEffect(registerResult) {
        when (registerResult) {
            RegisterResult.Success -> {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
                registerViewModel.resetRegisterResult()
            }

            RegisterResult.EmptyField -> {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                registerViewModel.resetRegisterResult()
            }

            RegisterResult.NetworkError -> {
                Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
                registerViewModel.resetRegisterResult()
            }

            null -> {
                // Do nothing
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
            onValueChange = { registerViewModel.updateEmail(it) },
            label = { Text("Email") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { registerViewModel.updateName(it) },
            label = { Text("Name") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        GenderSelection(selectedGender = gender, onGenderSelected = registerViewModel::updateGender)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { registerViewModel.updatePassword(it) },
            label = { Text("Password") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            trailingIcon = {
                val image =
                    if (passwordVisibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { registerViewModel.togglePasswordVisibility() }) {
                    Icon(imageVector = image, "Toggle password visibility")
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))


        Button(
            onClick = {
                coroutineScope.launch {
                    registerViewModel.register()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Register")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { navController.navigate("login") }
        ) {
            Text("Already have an account? Log In")
        }
    }
}