package com.himawan.gymstis.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.himawan.gymstis.ui.viewmodel.PeminjamanViewModel
import com.himawan.gymstis.ui.viewmodel.UpdateStatusResult
import kotlinx.serialization.ExperimentalSerializationApi


data class PeminjamanItem(
    val id: Long,
    val gender: Gender,
    val dayName: String,
    val date: String,
    val quota: String,
    val status: String
)

enum class Status(
    val value: String
) {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    DENIED("DENIED")
}

enum class FilterCriteriaPeminjaman {
    Ongoing, Completed, Male, Female, Pending, Approved, Denied
}

@ExperimentalSerializationApi
@ExperimentalMaterial3Api
@Composable
fun PeminjamanScreen(
    isStaff: Boolean = true,
    peminjamanViewModel: PeminjamanViewModel = viewModel(factory = PeminjamanViewModel.Factory)
) {
    val filteredItems by peminjamanViewModel.filteredPeminjamanItems.collectAsState()
    val selectedFilters = remember { mutableStateListOf<FilterCriteriaPeminjaman>() }
    val updateStatusResult by peminjamanViewModel.updateStatusResult.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(updateStatusResult) {
        when (updateStatusResult) {
            UpdateStatusResult.Success -> {
                Toast.makeText(context, "Status updated successfully", Toast.LENGTH_SHORT).show()
                peminjamanViewModel.resetUpdateStatusResult()
            }
            UpdateStatusResult.Error -> {
                Toast.makeText(context, "Error updating status", Toast.LENGTH_SHORT).show()
                peminjamanViewModel.resetUpdateStatusResult()
            }
            UpdateStatusResult.None -> {
                // do nothing
            }
        }
    }

    FilterChipGroup(selectedFilters = selectedFilters, onFilterChange = {
        peminjamanViewModel.setSelectedFilters(selectedFilters.toSet())
    })

    LazyColumn {
        items(filteredItems) { item ->
            PeminjamanItemRow(
                item = PeminjamanItem(
                    id = item.id,
                    gender = item.gender,
                    dayName = item.dayName,
                    date = item.date.toString(),
                    quota = "${item.peminjam}/${item.kuota}",
                    status = item.status
                ),
                isStaff = isStaff,
                viewModel = peminjamanViewModel
            )
            Divider()
        }
    }
}

@Composable
fun PeminjamanItemRow(item: PeminjamanItem, isStaff: Boolean, viewModel: PeminjamanViewModel) {
    var currentStatus by remember { mutableStateOf(item.status) }
    ListItem(
        headlineContent = { Text("${item.dayName} (${item.date})") },
        leadingContent = {
            Icon(
                imageVector = if (item.gender == Gender.MALE) Icons.Default.Male else Icons.Default.Female,
                contentDescription = if (item.gender == Gender.MALE) "Laki-laki" else "Perempuan"
            )
        },
        supportingContent = { Text("Kuota: ${item.quota}") },
        trailingContent = {
            if (isStaff) {
                Row {
                    IconButton(
                        onClick = {
                            viewModel.updatePeminjamanStatus(item.id, Status.APPROVED)
                            currentStatus = Status.APPROVED.value
                        },
                        enabled = currentStatus != Status.APPROVED.value
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Approve",
                            tint = if (currentStatus == Status.APPROVED.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.updatePeminjamanStatus(item.id, Status.DENIED)
                            currentStatus = Status.DENIED.value
                        },
                        enabled = currentStatus != Status.DENIED.value
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Deny",
                            tint = if (currentStatus == Status.DENIED.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            } else {
                when (item.status) {
                    "PENDING" -> Icon(Icons.Default.Info, contentDescription = "Pending")
                    "APPROVED" -> Icon(Icons.Default.CheckCircle, contentDescription = "Approved")
                    "DENIED" -> Icon(Icons.Default.Cancel, contentDescription = "Denied")
                    else -> {}
                }
            }

        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun FilterChipGroup(selectedFilters: SnapshotStateList<FilterCriteriaPeminjaman>, onFilterChange: () -> Unit) {
    Row {
        FilterCriteriaPeminjaman.values().forEach { criteria ->
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