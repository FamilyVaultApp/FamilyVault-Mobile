package com.github.familyvault.services

interface IPrivMxEndpointService {
    fun generatePrivateKey(password: String): String
}

expect fun createPrivMxService(): IPrivMxEndpointService