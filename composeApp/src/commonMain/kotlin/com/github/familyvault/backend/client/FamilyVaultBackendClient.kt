package com.github.familyvault.backend.client

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.requests.AddMemberToFamilyGroupRequest
import com.github.familyvault.backend.requests.CreateFamilyGroupRequest
import com.github.familyvault.backend.requests.ListMembersFromFamilyGroupRequest
import com.github.familyvault.backend.responses.CreateFamilyGroupResponse
import com.github.familyvault.backend.responses.ListMembersFromFamilyGroupResponse
import com.github.familyvault.backend.responses.PrivMxSolutionIdResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class FamilyVaultBackendClient : IFamilyVaultBackendClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    override suspend fun getSolutionId(): PrivMxSolutionIdResponse {
        return client.get(getEndpointUrl("/ApplicationConfig/GetSolutionId")).body()
    }

    override suspend fun createFamilyGroup(req: CreateFamilyGroupRequest): CreateFamilyGroupResponse {
        return client.post(getEndpointUrl("/FamilyGroup/CreateFamilyGroup")) {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(req))
        }.body<CreateFamilyGroupResponse>()
    }

    override suspend fun addGuardianToFamilyGroup(req: AddMemberToFamilyGroupRequest) {
        client.post(getEndpointUrl("/FamilyGroup/AddGuardianToFamilyGroup")) {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(req))
        }
    }

    override suspend fun addMemberToFamilyGroup(req: AddMemberToFamilyGroupRequest) {
        client.post(getEndpointUrl("/FamilyGroup/AddGuardianToFamilyGroup")) {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(req))
        }
    }

    override suspend fun addGuestToFamilyGroup(req: AddMemberToFamilyGroupRequest) {
        client.post(getEndpointUrl("/FamilyGroup/AddGuardianToFamilyGroup")) {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(req))
        }
    }

    override suspend fun listMembersOfFamilyGroup(req: ListMembersFromFamilyGroupRequest): ListMembersFromFamilyGroupResponse {
        return client.post(getEndpointUrl("/FamilyGroup/ListMembersFromFamilyGroup")) {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(req))
        }.body<ListMembersFromFamilyGroupResponse>()
    }

    private fun getEndpointUrl(endpoint: String): String {
        return "${AppConfig.BACKEND_URL}${endpoint}"
    }
}