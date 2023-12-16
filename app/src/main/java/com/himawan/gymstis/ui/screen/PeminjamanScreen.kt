package com.himawan.gymstis.ui.screen

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.himawan.gymstis.viewmodel.PeminjamanViewModel


data class PeminjamanItem(
    val gender: Gender,
    val dayName: String,
    val date: String,
    val quota: Int,
    val status: String
)


@Composable
fun PeminjamanScreen(
    isStaff: Boolean = true,
    peminjamanViewModel: PeminjamanViewModel = viewModel(factory = PeminjamanViewModel.Factory)
) {
    val peminjamanItems by peminjamanViewModel.peminjamanItems.collectAsState()

    LazyColumn {
        items(peminjamanItems) { item ->
            PeminjamanItemRow(
                item = PeminjamanItem(
                    gender = item.gender,
                    dayName = item.dayName,
                    date = item.date.toString(), // Format the date as needed
                    quota = item.kuota,
                    status = item.status
                ),
                isStaff = isStaff
            )
            Divider()
        }
    }
}

@Composable
fun PeminjamanItemRow(item: PeminjamanItem, isStaff: Boolean) {
    ListItem(
        headlineContent = { Text("${item.dayName} (${item.date})") },
        leadingContent = {
            Icon(
                imageVector = if (item.gender == Gender.MALE) Icons.Default.Male else Icons.Default.Female,
                contentDescription = if (item.gender == Gender.MALE) "Laki-laki" else "Perempuan"
            )
        },
        supportingContent = { Text("Kuota: ${item.quota}/10") },
        trailingContent = {
            if(isStaff){
                Row {
                    IconButton(onClick = { /* TODO: Implement Approve action */ }) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Approve")
                    }
                    IconButton(onClick = { /* TODO: Implement Deny action */ }) {
                        Icon(Icons.Default.Cancel, contentDescription = "Deny")
                    }
                }
            } else{
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