package com.paraooo.todolist.ui.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun getDateWithDot(date : LocalDate) : String {
    val formatter = DateTimeFormatter.ofPattern("yyyy. M. d.") // 원하는 포맷 설정
    val formattedDate = date.format(formatter)

    return formattedDate
}

fun LocalDate.toYMD() : String {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault())
    return this.format(formatter)
}