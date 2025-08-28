package com.paraooo.domain.repository

import java.time.LocalDate
import java.time.LocalTime

interface AlarmScheduler {

    fun schedule(date: LocalDate, time: LocalTime, templateId : Long)

    fun reschedule(date: LocalDate, time: LocalTime, templateId: Long)

    fun cancel(templateId: Long)
}