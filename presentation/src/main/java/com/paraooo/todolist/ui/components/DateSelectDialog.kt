package com.paraooo.todolist.ui.components

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelectDialog(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    showDialog : Boolean
) {
    if (showDialog) {

        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        onDateSelected(datePickerState.selectedDateMillis)
                        onDismiss()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Text("SELECT")
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White
            )
        ) {
            DatePicker(
                showModeToggle = false,
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    todayDateBorderColor = Color(0xFF54C392),
                    todayContentColor = Color(0xFF54C392),
                    selectedDayContentColor = Color.White,
                    selectedYearContentColor = Color.White,
                    selectedDayContainerColor = Color(0xFF54C392),
                    selectedYearContainerColor = Color(0xFF54C392),
                ),
            )
        }
    }
}

@Preview
@Composable
fun PreviewDateSelectDialog() {
    DateSelectDialog(
        {},
        {},
        true
    )
}