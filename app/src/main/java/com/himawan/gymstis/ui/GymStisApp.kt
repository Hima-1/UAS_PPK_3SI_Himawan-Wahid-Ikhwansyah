package com.himawan.gymstis.ui

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.himawan.gymstis.ui.component.BottomNavigationBar
import com.himawan.gymstis.ui.screen.ChangePasswordScreen
import com.himawan.gymstis.ui.screen.ChangeProfileScreen
import com.himawan.gymstis.ui.screen.CreateJadwalScreen
import com.himawan.gymstis.ui.screen.JadwalScreen
import com.himawan.gymstis.ui.screen.PeminjamanScreen
import com.himawan.gymstis.ui.screen.UserScreen

enum class Screen(val route: String, val icon: ImageVector, val title: String) {
    Jadwal("jadwal", Icons.Filled.Home, "Jadwal"),
    Peminjaman("peminjaman", Icons.Filled.CalendarToday, "Peminjaman"),
    User("user", Icons.Filled.Person, "User")
}

@ExperimentalMaterial3Api
@Preview
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GymStisApp() {
    MaterialTheme {
        val navController = rememberNavController()
        var selectedItem by remember { mutableIntStateOf(0) }

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        navBackStackEntry?.destination?.route?.let { route ->
            Screen.values().forEachIndexed { index, screen ->
                if (screen.route == route) {
                    selectedItem = index
                }
            }
        }

        val currentRoute = navBackStackEntry?.destination?.route

        val isStaff = true // Contoh, ganti sesuai logika aplikasi Anda

        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController, selectedItem) { selectedIndex ->
                    selectedItem = selectedIndex
                    navController.navigate(Screen.values()[selectedIndex].route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            },
            floatingActionButton = {
                if (currentRoute == Screen.Jadwal.route && isStaff) {
                    FloatingActionButton(
                        onClick = { navController.navigate("createJadwal") },
                        containerColor = MaterialTheme.colorScheme.secondary
                    ) {
                        Icon(Icons.Filled.Add, "Tambah Jadwal")
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) {
            NavHost(navController, startDestination = Screen.Jadwal.route) {
                composable(Screen.Jadwal.route) { JadwalScreen(isStaff) }
                composable(Screen.Peminjaman.route) { PeminjamanScreen(isStaff) }
                composable(Screen.User.route) { UserScreen(navController) }
                composable("changeProfile") { ChangeProfileScreen(navController) }
                composable("changePassword") { ChangePasswordScreen(navController) }
                composable("createJadwal") { CreateJadwalScreen(navController) }
            }
        }
    }
}
