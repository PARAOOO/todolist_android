package com.paraooo.domain.util

import com.paraooo.domain.model.Time
import java.time.LocalDate

interface AlarmScheduler {
    fun schedule(date: LocalDate, time: Time, templateId: Long)
    fun reschedule(date: LocalDate, time: Time, templateId: Long)
    fun cancel(templateId: Long)
}