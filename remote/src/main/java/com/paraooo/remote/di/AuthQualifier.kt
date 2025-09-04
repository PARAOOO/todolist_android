package com.paraooo.remote.di

import org.koin.core.qualifier.StringQualifier


// --- Koin을 사용할 경우 ---
object AuthQualifiers {
    val AUTHENTICATED = StringQualifier("Authenticated")
    val UNAUTHENTICATED = StringQualifier("Unauthenticated")
}
