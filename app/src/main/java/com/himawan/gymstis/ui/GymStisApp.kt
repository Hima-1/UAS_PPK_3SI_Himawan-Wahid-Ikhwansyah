package com.himawan.gymstis.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.himawan.gymstis.ui.component.BottomNavigationBar
import com.himawan.gymstis.ui.screen.EditPasswordScreen
import com.himawan.gymstis.ui.screen.EditProfileScreen
import com.himawan.gymstis.ui.screen.CreateJadwalScreen
import com.himawan.gymstis.ui.screen.JadwalScreen
import com.himawan.gymstis.ui.screen.LoginScreen
import com.himawan.gymstis.ui.screen.PeminjamanScreen
import com.himawan.gymstis.ui.screen.RegisterScreen
import com.himawan.gymstis.ui.screen.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymStisApp(
    navController: NavHostController = rememberNavController(),
    gymStisAppViewModel: GymStisAppViewModel = viewModel(factory = GymStisAppViewModel.Factory)
) {
    val loggedInUser = gymStisAppViewModel.userState.collectAsState().value
    val isStaff = loggedInUser.isStaff
    var selectedItem by remember { mutableStateOf(0) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    navBackStackEntry?.destination?.route?.let { route ->
        Screen.values().forEachIndexed { index, screen ->
            if (screen.route == route) {
                selectedItem = index
            }
        }
    }
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, selectedItem) { selectedIndex ->
                selectedItem = selectedIndex
                navController.navigate(Screen.values()[selectedIndex].route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
            BottomNavigationBar(navController, selectedItem) { selectedIndex ->
                selectedItem = selectedIndex
                navController.navigate(Screen.values()[selectedIndex].route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
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
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") { LoginScreen(navController) }
            composable("register") { RegisterScreen(navController) }
            composable(Screen.Jadwal.route) { JadwalScreen(isStaff) }
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
