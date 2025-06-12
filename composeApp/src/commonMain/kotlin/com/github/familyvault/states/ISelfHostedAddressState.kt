package com.github.familyvault.states

interface ISelfHostedAddressState {
    fun set(address: String)
    fun get(): String?
    fun reset()
}