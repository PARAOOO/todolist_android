package com.paraooo.remote.dto.request

data class VerifyCodeRequestDto(
    val email: String,
    val code: String
)
