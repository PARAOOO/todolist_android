package com.paraooo.domain.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun getDateWithDot(date : LocalDate) : String {
    val formatter = DateTimeFormatter.ofPattern("yyyy. M. d") // 원하는 포맷 설정
    val formattedDate = date.format(formatter)

    return formattedDate
}

fun getDateOfWeekEEE(date : LocalDate) : String {
    val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEE")
    val dayOfWeek = date.format(dayOfWeekFormatter)

    return dayOfWeek
}

fun transferMillis2LocalDate( millis : Long?) : LocalDate {
    if(millis != null) {
        val transferredDate: LocalDate = millis.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }

        return transferredDate
    }
    else {
        return LocalDate.now()
    }
}

fun transferLocalDateToMillis(localDate: LocalDate?): Long {
    return localDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
        ?: System.currentTimeMillis()
}

fun getDateDiff(date1 : LocalDate, date2 : LocalDate) : Int {
    return ChronoUnit.DAYS.between(date1, date2).toInt()
}