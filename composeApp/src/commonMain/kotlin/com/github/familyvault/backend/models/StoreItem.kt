package com.github.familyvault.backend.models

data class StoreItem(
    val id: String,
    val managers: List<String>,
    val users: List<String>,
    val publicMeta: StorePublicMeta,
)