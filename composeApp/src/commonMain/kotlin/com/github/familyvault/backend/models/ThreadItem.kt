package com.github.familyvault.backend.models

data class ThreadItem(
    val threadId: String,
    val managers: List<String>,
    val users: List<String>,
    val decodedThreadTag: String,
    val decodedThreadName: String
)