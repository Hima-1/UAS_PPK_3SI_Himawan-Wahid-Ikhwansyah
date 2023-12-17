package com.himawan.gymstis.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.himawan.gymstis.ui.viewmodel.ActionStatus
import com.himawan.gymstis.ui.viewmodel.JadwalViewModel
import kotlinx.serialization.ExperimentalSerializationApi

data class JadwalItem(
    val gender: String,
    val dayName: String,
    val date: String,
    val quota: String
)

enum class FilterCriteriaJadwal {
    Ongoing, Completed, Male, Female
}

@ExperimentalSerializationApi
@ExperimentalMaterial3Api
@Composable
fun JadwalScreen(
    isStaff: Boolean,
    navController: NavHostController,
    jadwalViewModel: JadwalViewModel = viewModel(factory = JadwalViewModel.Factory)
) {
    val filteredJadwals by jadwalViewModel.filteredJadwals.collectAsState()
    val selectedFilters = remember { mutableStateListOf<FilterCriteriaJadwal>() }

    val context = LocalContext.current
    val deleteStatus by jadwalViewModel.deleteStatus.collectAsState()
    val applyStatus by jadwalViewModel.applyStatus.collectAsState()

    LaunchedEffect(deleteStatus) {
        when (deleteStatus) {
            ActionStatus.Success -> {
                Toast.makeText(context, "Jadwal deleted successfully", Toast.LENGTH_SHORT).show()
                jadwalViewModel.resetActionStatus()
            }

            ActionStatus.Error -> {
                Toast.makeText(context, "Error deleting Jadwal", Toast.LENGTH_SHORT).show()
                jadwalViewModel.resetActionStatus()
            }

            ActionStatus.None -> {}
        }
    }

    LaunchedEffect(applyStatus) {
        when (applyStatus) {
            ActionStatus.Success -> {
                Toast.makeText(context, "Applied for Jadwal successfully", Toast.LENGTH_SHORT)
                    .show()
                jadwalViewModel.resetActionStatus()
            }

            ActionStatus.Error -> {
                Toast.makeText(context, "Error applying for Jadwal", Toast.LENGTH_SHORT).show()
                jadwalViewModel.resetActionStatus()
            }

            ActionStatus.None -> {}
        }
    }

    FilterChipGroup(
        selectedFilters = selectedFilters,
        onFilterChange = {
            jadwalViewModel.setSelectedFilters(selectedFilters.toSet())
        })


    LazyColumn {
        items(filteredJadwals) { jadwal ->
            JadwalItemRow(
                item = JadwalItem(
                    gender = jadwal.gender,
                    dayName = jadwal.hari,
                    date = jadwal.date.toString(),
                    quota = "${jadwal.peminjam}/${jadwal.kuota}"
                ),
                isStaff = isStaff,
                onDeleteClick = { jadwalViewModel.deleteJadwal(jadwal.id) }
            ) { jadwalViewModel.applyForJadwal(jadwal.date) }
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

@ExperimentalMaterial3Api
@Composable
fun FilterChipGroup(
    selectedFilters: SnapshotStateList<FilterCriteriaJadwal>,
    onFilterChange: () -> Unit
) {
    Row {
        FilterCriteriaJadwal.values().forEach { criteria ->
            FilterChip(
                selected = criteria in selectedFilters,
                onClick = {
                    if (criteria in selectedFilters) {
                        selectedFilters.remove(criteria)
                    } else {
                        selectedFilters.add(criteria)
                    }
                    onFilterChange()
                },
                label = { Text(criteria.name) }
            )
        }
    }
}