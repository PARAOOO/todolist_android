package com.paraooo.domain.model

import org.jetbrains.annotations.Nullable
import java.time.LocalDate

sealed class UseCaseResult<out T> {
    data class Success<out T>(val data: T) : UseCaseResult<T>()
    data class Error(val exception: Exception) : UseCaseResult<Nothing>()
    data class Failure(val message: String) : UseCaseResult<Nothing>()
}