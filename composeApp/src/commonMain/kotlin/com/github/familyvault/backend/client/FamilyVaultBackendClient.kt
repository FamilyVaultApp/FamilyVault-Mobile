package com.github.familyvault.backend.client

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.requests.AddMemberToFamilyGroupRequest
import com.github.familyvault.backend.requests.CreateFamilyGroupRequest
import com.github.familyvault.backend.requests.GetTokenStatusRequest
import com.github.familyvault.backend.requests.ListMembersFromFamilyGroupRequest
import com.github.familyvault.backend.requests.UpdateTokenStatusRequest
import com.github.familyvault.backend.responses.CreateFamilyGroupResponse
import com.github.familyvault.backend.responses.GenerateJoinTokenResponse
import com.github.familyvault.backend.responses.GetTokenStatusResponse
import com.github.familyvault.backend.responses.ListMembersFromFamilyGroupResponse
import com.github.familyvault.backend.responses.PrivMxSolutionIdResponse
import com.github.familyvault.backend.responses.UpdateTokenStatusResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.parameter
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

    private suspend inline fun <reified T> getRequest(endpoint: String, req: Any?): T {
        val response = client.get(getEndpointUrl(endpoint)) {
            contentType(ContentType.Application.Json)
            TODO("Obsługa parametrów zapytania")
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

    override suspend fun addGuardianToFamilyGroup(req: AddMemberToFamilyGroupRequest) {
        postRequest<Unit>("/FamilyGroup/AddGuardianToFamilyGroup", req)
    }

    override suspend fun addMemberToFamilyGroup(req: AddMemberToFamilyGroupRequest) {
        postRequest<Unit>("/FamilyGroup/AddMemberToFamilyGroup", req)
    }

    override suspend fun addGuestToFamilyGroup(req: AddMemberToFamilyGroupRequest) {
        postRequest<Unit>("/FamilyGroup/AddGuestToFamilyGroup", req)
    }

    override suspend fun listMembersOfFamilyGroup(req: ListMembersFromFamilyGroupRequest): ListMembersFromFamilyGroupResponse {
        return postRequest<ListMembersFromFamilyGroupResponse>(
            "/FamilyGroup/ListMembersFromFamilyGroup", req
        )
    }

    override suspend fun generateJoinToken(): GenerateJoinTokenResponse {
        return client.get(getEndpointUrl("/JoinStatus/Generate")).body()
    }

    override suspend fun getTokenStatus(req: GetTokenStatusRequest): GetTokenStatusResponse {
        return client.get(getEndpointUrl("/JoinStatus/GetByToken")) {
            parameter("token", req.token)
        }.body()
    }

    override suspend fun updateTokenStatus(req: UpdateTokenStatusRequest): UpdateTokenStatusResponse {
        return postRequest<UpdateTokenStatusResponse>(
            "/JoinStatus/UpdateStatus", req
        )
    }


    private fun getEndpointUrl(endpoint: String): String {
        return "${AppConfig.BACKEND_URL}$endpoint"
    }
}