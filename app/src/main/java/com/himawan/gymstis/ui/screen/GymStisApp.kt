package com.himawan.gymstis.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.himawan.gymstis.ui.component.BottomNavigationBar
import com.himawan.gymstis.ui.viewmodel.GymStisAppViewModel
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymStisApp(
    navController: NavHostController = rememberNavController(),
    gymStisAppViewModel: GymStisAppViewModel = viewModel(factory = GymStisAppViewModel.Factory)
) {
    val loggedInUser = gymStisAppViewModel.userState.collectAsState().value
    val isStaff = loggedInUser.isStaff
    var selectedItem by remember { mutableIntStateOf(0) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    navBackStackEntry?.destination?.route?.let { route ->
        Screen.values().forEachIndexed { index, screen ->
            if (screen.route == route) {
                selectedItem = index
            }
        }
    }
    val startDestination = if (loggedInUser.token.isNotEmpty()) "jadwal" else "login"

    Scaffold(
        bottomBar = {
            if (loggedInUser.token.isNotEmpty()) {
                    BottomNavigationBar(navController, selectedItem) { selectedIndex ->
                        selectedItem = selectedIndex
                        navController.navigate(Screen.values()[selectedIndex].route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
        },
        floatingActionButton = {
            if (isStaff && navBackStackEntry?.destination?.route == Screen.Jadwal.route) {
                FloatingActionButton(
                    onClick = { navController.navigate("createJadwal") },
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(Icons.Filled.Add, "Tambah Jadwal")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") { LoginScreen(navController) }
            composable("register") { RegisterScreen(navController) }
            composable(Screen.Jadwal.route) { JadwalScreen(isStaff, navController) }
            composable(Screen.Peminjaman.route) { PeminjamanScreen(isStaff) }
            composable(Screen.User.route) { ProfileScreen(navController, isStaff) }
            composable("changeProfile") { EditProfileScreen(navController) }
            composable("changePassword") { EditPasswordScreen(navController) }
            composable("createJadwal") { CreateJadwalScreen(navController) }
            composable("manageUser") {}
        }
    }
}

enum class Screen(val route: String, val icon: ImageVector, val title: String) {
    Jadwal("jadwal", Icons.Filled.Home, "Jadwal"),
    Peminjaman("peminjaman", Icons.Filled.CalendarToday, "Peminjaman"),
    User("user", Icons.Filled.Person, "User")
}
