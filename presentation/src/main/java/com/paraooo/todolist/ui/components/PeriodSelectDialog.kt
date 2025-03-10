package com.paraooo.todolist.ui.components

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodSelectDialog(
    onPeriodSelected: (startDate : Long?, endDate : Long?) -> Unit,
    onDismiss: () -> Unit,
    showDialog : Boolean
) {
    if (showDialog) {

        val dateRangePickerState = rememberDateRangePickerState()

        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        onPeriodSelected(
                            dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis
                        )
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
            DateRangePicker(
                showModeToggle = false,
                state = dateRangePickerState,
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
fun PreviewPeriodSelectDialog() {
    PeriodSelectDialog(
        { start, end -> },
        {},
        true
    )
}