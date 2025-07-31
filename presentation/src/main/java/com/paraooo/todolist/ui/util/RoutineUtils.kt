package com.paraooo.todolist.ui.util

import com.paraooo.domain.model.RoutineIconModel
import com.paraooo.todolist.R

fun getRoutineIconDrawbleId(routineIcon : RoutineIconModel) : Int{
    when(routineIcon) {
        RoutineIconModel.ICON1 -> {
            return R.drawable.ic_routine_1
        }
        RoutineIconModel.ICON2 -> {
            return R.drawable.ic_routine_2
        }
        RoutineIconModel.ICON3 -> {
            return R.drawable.ic_routine_3
        }
    }
}