package com.himawan.gymstis.ui.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.himawan.gymstis.viewmodel.JadwalViewModel

data class JadwalItem(
    val gender: String,
    val dayName: String,
    val date: String,
    val quota: String
    // Add any other relevant fields
)

@Composable
fun JadwalScreen(
    isStaff: Boolean,
    navController: NavHostController,
    jadwalViewModel: JadwalViewModel = viewModel(factory = JadwalViewModel.Factory)
) {
    val jadwals by jadwalViewModel.jadwals.collectAsState()

    LazyColumn {
        items(jadwals) { jadwal ->
            JadwalItemRow(
                item = JadwalItem(
                    gender = jadwal.gender,
                    dayName = jadwal.hari,
                    date = jadwal.date.toString(), // Format the date as needed
                    quota = "${jadwal.peminjam}/${jadwal.kuota}"
                ),
                isStaff = isStaff,
                onDeleteClick = { jadwalViewModel.deleteJadwal(jadwal.id) }
            ) { jadwalViewModel.applyForJadwal(jadwal.id, jadwal.date) }
            Divider()
        }
    }
}

@Composable
fun JadwalItemRow(
    item: JadwalItem,
    isStaff: Boolean,
    onDeleteClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text("${item.dayName} (${item.date})") },
        leadingContent = {
            Icon(
                imageVector = if (item.gender == "MALE") Icons.Default.Male else Icons.Default.Female,
                contentDescription = if (item.gender == "MALE") "Laki-laki" else "Perempuan"
            )
        },
        trailingContent = {
            if (isStaff) {
                Button(onClick = onDeleteClick) {
                    Text("Hapus")
                }
            } else {
                Button(onClick = onApplyClick) {
                    Text("Apply")
                }
            }
        },
        supportingContent = { Text("Kuota: ${item.quota}") }
    )
}