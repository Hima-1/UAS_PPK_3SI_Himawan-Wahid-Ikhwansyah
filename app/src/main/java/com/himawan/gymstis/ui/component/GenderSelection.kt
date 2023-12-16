package com.himawan.gymstis.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.himawan.gymstis.ui.screen.Gender

@Composable
fun GenderSelection(selectedGender: Gender, onGenderSelected: (Gender) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        GenderButton(
            icon = Icons.Default.Male,
            label = "Male",
            isSelected = selectedGender == Gender.MALE,
            onSelected = { onGenderSelected(Gender.MALE) }
        )
        GenderButton(
            icon = Icons.Default.Female,
            label = "Female",
            isSelected = selectedGender == Gender.FEMALE,
            onSelected = { onGenderSelected(Gender.FEMALE) }
        )
    }
}

@Composable
fun GenderButton(icon: ImageVector, label: String, isSelected: Boolean, onSelected: () -> Unit) {
    OutlinedButton(
        onClick = onSelected,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    ) {
        Icon(imageVector = icon, contentDescription = label)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    }
}