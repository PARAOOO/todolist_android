package com.paraooo.todolist.ui.features.routine_create

import com.paraooo.domain.model.RootRoutineModel
import com.paraooo.domain.model.RoutineColorModel
import com.paraooo.domain.model.RoutineIconModel
import com.paraooo.domain.model.TodoModel
import java.time.LocalDate
import java.time.LocalTime

data class RootRoutineInputState(
    val routineName : String = "",
    val time : LocalTime = LocalTime.now(),
    val dayOfWeek : List<Int> = listOf(),
    val color : RoutineColorModel = RoutineColorModel.BLUE,
    val icon : RoutineIconModel = RoutineIconModel.ICON1
)

data class RoutineCreateUiState(
    val routine : RootRoutineModel? = null,
    val rootRoutineInput : RootRoutineInputState = RootRoutineInputState()
)
