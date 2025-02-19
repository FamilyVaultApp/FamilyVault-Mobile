package com.github.familyvault.backend

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.requests.CreateFamilyGroupRequest
import com.github.familyvault.backend.responses.CreateFamilyGroupResponse
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

class FamilyVaultBackendProxy {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun getSolutionId(): PrivMxSolutionIdResponse {
        return client.get(getEndpointUrl("/ApplicationConfig/GetSolutionId")).body()
    }

    suspend fun createFamilyGroup(req: CreateFamilyGroupRequest) : CreateFamilyGroupResponse {
        return client.post(getEndpointUrl("/FamilyGroup/CreateFamilyGroup")) {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(req))
        }.body<CreateFamilyGroupResponse>();
    }

    private fun getEndpointUrl(endpoint: String): String {
        return "${AppConfig.BACKEND_URL}${endpoint}"
    }
}