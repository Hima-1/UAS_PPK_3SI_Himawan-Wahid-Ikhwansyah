package com.himawan.gymstis.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.himawan.gymstis.R
import com.himawan.gymstis.ui.viewmodel.EditPasswordViewModel
import com.himawan.gymstis.ui.viewmodel.PasswordChangeResult

@Composable
fun EditPasswordScreen(
    navController: NavController,
    editPasswordViewModel: EditPasswordViewModel = viewModel(factory = EditPasswordViewModel.Factory)
) {
    val context = LocalContext.current
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val passwordChangeResult by editPasswordViewModel.passwordChangeResult.collectAsState()

    LaunchedEffect(passwordChangeResult) {
        when (passwordChangeResult) {
            PasswordChangeResult.Success -> {
                Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show()
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
                editPasswordViewModel.resetPasswordChangeResult()
            }
            PasswordChangeResult.Error -> {
                Toast.makeText(context, "Error changing password", Toast.LENGTH_SHORT).show()
                editPasswordViewModel.resetPasswordChangeResult()
            }
            PasswordChangeResult.None -> {

            }
            PasswordChangeResult.BadInput -> {
                Toast.makeText(context, "Bad input", Toast.LENGTH_SHORT).show()
                editPasswordViewModel.resetPasswordChangeResult()}
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