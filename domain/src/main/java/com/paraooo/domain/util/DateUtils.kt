package com.paraooo.domain.util

import com.paraooo.domain.model.Time
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


fun getDateOfWeekEEE(date : LocalDate) : String {
    val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEE")
    val dayOfWeek = date.format(dayOfWeekFormatter)

    return dayOfWeek
}

fun transferMillis2LocalDate(millis : Long?) : LocalDate {
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

fun transferLocalDateTimeToMillis(localDateTime : LocalDateTime): Long {
    return localDateTime.atZone(ZoneId.systemDefault()) // 시스템 기본 시간대 사용
        .toInstant()
        .toEpochMilli()
}

fun getDateDiff(date1 : LocalDate, date2 : LocalDate) : Int {
    return ChronoUnit.DAYS.between(date1, date2).toInt()
}

fun todoToMillis(date : LocalDate, time : Time) : Long {
    val millis = LocalDateTime.of(
        date.year,
        date.month,
        date.dayOfMonth,
        time.hour,
        time.minute
    ).atZone(ZoneId.systemDefault())  // 현지 시간 기준
        .toInstant()
        .toEpochMilli()

    val transferredTime: LocalDateTime = millis.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    return millis
}