package com.paraooo.local.database

interface TransactionProvider {
    suspend fun <R> runInTransaction(block: suspend () -> R): R
}