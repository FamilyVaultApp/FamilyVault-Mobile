package com.github.familyvault.models

import kotlinx.serialization.Serializable

@Serializable
data class MemberIdentifier(val id: String, val firstname: String, val surname: String?) {
    val fullname: String
        get() = if (surname != "") "$firstname $surname" else firstname
}