package com.paraooo.todolist.ui.features.routine_create.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paraooo.domain.model.RootRoutineModel

@Composable
fun RoutineList(
    modifier : Modifier = Modifier,
    routine : RootRoutineModel
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(bottom = 5.dp),
    ) {
        RootRoutineCard(
            routine = routine
        )

        Spacer(modifier = Modifier.height(5.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(routine.subRoutines) { subRoutine ->
                SubRoutineCard(
                    routine = subRoutine,
                    color = routine.color
                )
            }
        }
    }
}