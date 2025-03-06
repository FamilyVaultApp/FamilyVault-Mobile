package com.github.familyvault.backend.client

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.requests.AddMemberToFamilyRequest
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
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json

class FamilyVaultBackendClient : IFamilyVaultBackendClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    private suspend inline fun <reified T> postRequest(endpoint: String, req: Any): T {
        val response = client.post(getEndpointUrl(endpoint)) {
            contentType(ContentType.Application.Json)
            setBody(req)
        }
        if (response.status.isSuccess()) {
            return response.body<T>()
        } else {
            throw FamilyVaultBackendErrorResponseException(
                response.status,
                response.body<String>()
            )
        }

    }

    override suspend fun getSolutionId(): PrivMxSolutionIdResponse {
        return client.get(getEndpointUrl("/ApplicationConfig/GetSolutionId")).body()
    }

    override suspend fun createFamilyGroup(req: CreateFamilyGroupRequest): CreateFamilyGroupResponse {
        return postRequest<CreateFamilyGroupResponse>(
            "/FamilyGroup/CreateFamilyGroup", req
        )
    }

    override suspend fun addGuardianToFamilyGroup(req: AddMemberToFamilyRequest) {
        postRequest<Unit>("/FamilyGroup/AddGuardianToFamilyGroup", req)
    }

    override suspend fun addMemberToFamilyGroup(req: AddMemberToFamilyRequest) {
        postRequest<Unit>("/FamilyGroup/AddMemberToFamilyGroup", req)
    }

    override suspend fun addGuestToFamilyGroup(req: AddMemberToFamilyRequest) {
        postRequest<Unit>("/FamilyGroup/AddGuestToFamilyGroup", req)
    }

    private fun getEndpointUrl(endpoint: String): String {
        return "${AppConfig.BACKEND_URL}$endpoint"
    }
}