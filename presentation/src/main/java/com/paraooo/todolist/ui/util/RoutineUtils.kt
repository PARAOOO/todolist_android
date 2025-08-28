package com.paraooo.todolist.ui.util

import com.paraooo.todolist.R


private val iconList = listOf(
    R.drawable.ic_icon_1,
    R.drawable.ic_icon_2,
    R.drawable.ic_icon_3,
    R.drawable.ic_icon_4,
    R.drawable.ic_icon_5,
    R.drawable.ic_icon_6,
    R.drawable.ic_icon_7,
    R.drawable.ic_icon_8,
    R.drawable.ic_icon_9,
    R.drawable.ic_icon_10,
    R.drawable.ic_icon_11,
    R.drawable.ic_icon_12,
    R.drawable.ic_icon_13,
    R.drawable.ic_icon_14,
    R.drawable.ic_icon_15,
    R.drawable.ic_icon_16,
    R.drawable.ic_icon_17,
    R.drawable.ic_icon_18,
    R.drawable.ic_icon_19,
    R.drawable.ic_icon_20,
    R.drawable.ic_icon_21,
)

internal fun getRoutineIconDrawableId(routineIcon : Int) : Int{

    return iconList[routineIcon-1]
}

internal fun getRoutineIconDrawableIdList() : List<Int>{
    return iconList
}