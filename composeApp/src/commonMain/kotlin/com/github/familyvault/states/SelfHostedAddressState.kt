package com.github.familyvault.states

class SelfHostedAddressState : ISelfHostedAddressState {
    private var selfHostedAddress: String? = null

    override fun set(address: String) {
        selfHostedAddress = address
    }

    override fun get(): String?
        = selfHostedAddress

    override fun reset() {
        selfHostedAddress = null
    }
}