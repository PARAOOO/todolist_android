package com.paraooo.domain.repository

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

interface AlarmScheduler {

    fun schedule(date: LocalDate, time: LocalTime, templateId : UUID)

    fun reschedule(date: LocalDate, time: LocalTime, templateId: UUID)

    fun cancel(templateId: UUID)
}