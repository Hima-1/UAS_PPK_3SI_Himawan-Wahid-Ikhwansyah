package com.himawan.gymstis.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun UserScreen(navController: NavController) {
    val userName = "John Doe" // Replace with actual user name
    val gender = "MALE" // Replace with actual gender value
    val role = "User" // Replace with actual role

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // User icon and details
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(150.dp)
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .align(Alignment.CenterHorizontally)
                .offset(y = (-25).dp)
                .background(Color.LightGray, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                modifier = Modifier.size(100.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = userName, style = MaterialTheme.typography.headlineMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (gender == "MALE") Icons.Default.Male else Icons.Default.Female,
                contentDescription = "Gender",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = role, style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // User icon and details
            // ... (Keep the user details section as it is)

            Spacer(modifier = Modifier.height(24.dp))

            // Options List
            OptionItem(Icons.Default.Edit, "Change Profile") {
                navController.navigate("changeProfile")
            }
            OptionItem(Icons.Default.VpnKey, "Change Password") {
                navController.navigate("changePassword")
            }
            OptionItem(Icons.Default.ExitToApp, "Logout") {
                // Handle Logout action
            }
        }
    }
}

@Composable
fun OptionItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // Add clickable here
        leadingContent = { Icon(icon, contentDescription = null) },
        headlineContent = { Text(text) }
    )
    Divider() // Optional: Adds a divider line between the items
}