package com.github.familyvault.models

data class FamilyGroupSession(
    var bridgeUrl: String,
    val familyGroupName: String,
    var solutionId: String,
    var contextId: String,
    var keyPair: PublicPrivateKeyPair,
)