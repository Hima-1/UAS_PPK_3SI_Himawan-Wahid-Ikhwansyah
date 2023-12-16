package com.himawan.gymstis.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.himawan.gymstis.ui.screen.Screen

@Composable
fun BottomNavigationBar(navController: NavController, selectedItem: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar {
        Screen.values().forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) }
            )
        }
    }
}