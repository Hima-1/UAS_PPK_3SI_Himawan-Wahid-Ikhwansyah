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


data class PeminjamanItem(
    val gender: String,
    val dayName: String,
    val date: String,
    val quota: Int,
    val status: String
)


@Composable
fun PeminjamanScreen(isStaff: Boolean = true) {
    // Sample data
    val items = listOf(
        PeminjamanItem("MALE", "Senin", "12 Des 2023", 7, "PENDING"),
        PeminjamanItem("FEMALE", "Selasa", "13 Des 2023", 5, "APPROVED"),
        PeminjamanItem("MALE", "Rabu", "14 Des 2023", 3, "PENDING"),
        PeminjamanItem("FEMALE", "Kamis", "15 Des 2023", 4, "APPROVED"),
        PeminjamanItem("MALE", "Jumat", "16 Des 2023", 6, "PENDING"),
        PeminjamanItem("FEMALE", "Sabtu", "17 Des 2023", 2, "APPROVED"),
        PeminjamanItem("MALE", "Minggu", "18 Des 2023", 8, "PENDING"),
        PeminjamanItem("FEMALE", "Senin", "19 Des 2023", 5, "APPROVED"),
        PeminjamanItem("MALE", "Selasa", "20 Des 2023", 3, "PENDING"),
        PeminjamanItem("FEMALE", "Rabu", "21 Des 2023", 7, "APPROVED"),
        PeminjamanItem("MALE", "Kamis", "22 Des 2023", 4, "PENDING"),
        // Add 10 more items here...
    )

    LazyColumn {
        items(items) { item ->
            PeminjamanItemRow(item, isStaff)
            Divider()
        }
    }
}

// Now, the 'items' list contains 12 'PeminjamanItem' objects.

@Composable
fun PeminjamanItemRow(item: PeminjamanItem, isStaff: Boolean) {
    ListItem(
        headlineContent = { Text("${item.dayName} (${item.date})") },
        leadingContent = {
            Icon(
                imageVector = if (item.gender == "MALE") Icons.Default.Male else Icons.Default.Female,
                contentDescription = if (item.gender == "MALE") "Laki-laki" else "Perempuan"
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