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
import androidx.navigation.NavHostController
import com.himawan.gymstis.R
import com.himawan.gymstis.ui.viewmodel.LoginResult
import com.himawan.gymstis.ui.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    var passwordVisibility by remember { mutableStateOf(false) }
    val loginResult by loginViewModel.loginResult.collectAsState()

    if (loginResult == LoginResult.Success) {
        navController.navigate("jadwal"){
            popUpTo("jadwal") { inclusive = true}
        }
        loginViewModel.resetLoginResult()
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
            value = loginViewModel.email,
            onValueChange = { loginViewModel.updateEmail(it)},
            label = { Text("Email") },
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = loginViewModel.password,
            onValueChange = { loginViewModel.updatePassword(it) },
            label = { Text("Password") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            trailingIcon = {
                val image =
                    if (passwordVisibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, "Toggle password visibility")
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    loginViewModel.login()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { navController.navigate("register") }
        ) {
            Text("Don't have an account? Sign Up")
        }
    }
}