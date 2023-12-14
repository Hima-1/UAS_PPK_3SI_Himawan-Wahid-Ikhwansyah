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
import androidx.compose.ui.tooling.preview.Preview

data class JadwalItem(
    val gender: String,
    val dayName: String,
    val date: String,
    val quota: Int
    // Add any other relevant fields
)

@Composable
fun JadwalScreen(isStaff: Boolean) {
    val items = listOf(
        JadwalItem("MALE", "Senin", "12 Des 2023", 7),
        JadwalItem("FEMALE", "Selasa", "13 Des 2023", 5),
        // Add more items...
    )

    LazyColumn {
        items(items) { item ->
            JadwalItemRow(item, isStaff)
            Divider()
        }
    }
}

@Composable
fun JadwalItemRow(item: JadwalItem, isStaff: Boolean) {
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
                Button(onClick = { /* TODO: Implement Edit action */ }) {
                    Text("Hapus")
                }
            } else {
                Button(onClick = { /* TODO: Implement Apply action */ }) {
                    Text("Apply")
                }
            }
        },
        supportingContent = { Text("Kuota: ${item.quota}/10") }
    )
}