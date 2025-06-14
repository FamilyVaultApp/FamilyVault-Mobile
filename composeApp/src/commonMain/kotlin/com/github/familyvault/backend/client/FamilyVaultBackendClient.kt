package com.github.familyvault.backend.client

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.exceptions.FamilyVaultBackendErrorException
import com.github.familyvault.backend.exceptions.FamilyVaultBackendNoConnectionException
import com.github.familyvault.backend.requests.AddMemberToFamilyGroupRequest
import com.github.familyvault.backend.requests.ChangeFamilyMemberPermissionGroupRequest
import com.github.familyvault.backend.requests.CreateFamilyGroupRequest
import com.github.familyvault.backend.requests.DeleteJoinStatusRequest
import com.github.familyvault.backend.requests.FamilyVaultBackendRequest
import com.github.familyvault.backend.requests.GetFamilyGroupNameRequest
import com.github.familyvault.backend.requests.GetJoinStatusRequest
import com.github.familyvault.backend.requests.GetMemberFromFamilyGroupRequest
import com.github.familyvault.backend.requests.ListMembersFromFamilyGroupRequest
import com.github.familyvault.backend.requests.RemoveMemberFromFamilyGroupRequest
import com.github.familyvault.backend.requests.RenameFamilyGroupRequest
import com.github.familyvault.backend.requests.UpdateJoinStatusRequest
import com.github.familyvault.backend.responses.AddMemberToFamilyGroupResponse
import com.github.familyvault.backend.responses.ChangeFamilyMemberPermissionGroupResponse
import com.github.familyvault.backend.responses.CreateFamilyGroupResponse
import com.github.familyvault.backend.responses.DeleteJoinStatusResponse
import com.github.familyvault.backend.responses.FamilyVaultBackendResponse
import com.github.familyvault.backend.responses.GenerateJoinStatusResponse
import com.github.familyvault.backend.responses.GetBridgeUrlResponse
import com.github.familyvault.backend.responses.GetFamilyGroupNameResponse
import com.github.familyvault.backend.responses.GetJoinStatusResponse
import com.github.familyvault.backend.responses.GetMemberFromFamilyGroupResponse
import com.github.familyvault.backend.responses.ListMembersFromFamilyGroupResponse
import com.github.familyvault.backend.responses.PrivMxSolutionIdResponse
import com.github.familyvault.backend.responses.RemoveMemberFromFamilyGroupResponse
import com.github.familyvault.backend.responses.RenameFamilyGroupResponse
import com.github.familyvault.backend.responses.UpdateJoinStatusResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json

class FamilyVaultBackendClient : IFamilyVaultBackendClient {
    private var customServerUrl: String? = null

    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    override fun setCustomBackendUrl(url: String) {
        customServerUrl = url
    }

    override fun removeCustomBackendUrl() {
        customServerUrl = null
    }

    override suspend fun getSolutionId(): PrivMxSolutionIdResponse {
        return getRequest("/ApplicationConfig/GetSolutionId")
    }

    override suspend fun getBridgeUrl(): GetBridgeUrlResponse {
        return getRequest("/ApplicationConfig/GetBridgeUrl")
    }

    override suspend fun createFamilyGroup(req: CreateFamilyGroupRequest): CreateFamilyGroupResponse {
        return postRequest(
            "/FamilyGroup/CreateFamilyGroup", req
        )
    }

    override suspend fun addMemberToFamilyGroup(req: AddMemberToFamilyGroupRequest): AddMemberToFamilyGroupResponse {
        return postRequest("/FamilyGroup/AddMemberToFamilyGroup", req)
    }

    override suspend fun changeFamilyMemberPermissionGroup(req: ChangeFamilyMemberPermissionGroupRequest): ChangeFamilyMemberPermissionGroupResponse {
        return postRequest("/FamilyGroup/ChangeMemberPermissionGroup", req)
    }

    override suspend fun listMembersOfFamilyGroup(req: ListMembersFromFamilyGroupRequest): ListMembersFromFamilyGroupResponse {
        return postRequest(
            "/FamilyGroup/ListMembersFromFamilyGroup", req
        )
    }

    override suspend fun getMemberFromFamilyGroup(req: GetMemberFromFamilyGroupRequest): GetMemberFromFamilyGroupResponse {
        return postRequest(
            "/FamilyGroup/GetMemberFromFamilyGroup", req
        )
    }

    override suspend fun renameFamilyGroup(req: RenameFamilyGroupRequest): RenameFamilyGroupResponse {
        return postRequest("/FamilyGroup/Rename", req)
    }

    override suspend fun removeMemberFromFamilyGroup(req: RemoveMemberFromFamilyGroupRequest): RemoveMemberFromFamilyGroupResponse {
        return postRequest("/FamilyGroup/RemoveMemberFromFamilyGroup", req)
    }

    override suspend fun getFamilyGroupName(req: GetFamilyGroupNameRequest): GetFamilyGroupNameResponse {
        return postRequest("/FamilyGroup/GetFamilyGroupName", req)
    }

    override suspend fun generateJoinStatus(): GenerateJoinStatusResponse {
        return getRequest("/JoinStatus/Generate")
    }

    override suspend fun getJoinStatus(req: GetJoinStatusRequest): GetJoinStatusResponse {
        return getRequest("/JoinStatus/GetByToken", req)
    }

    override suspend fun updateJoinStatus(req: UpdateJoinStatusRequest): UpdateJoinStatusResponse {
        return postRequest(
            "/JoinStatus/UpdateStatus", req
        )
    }

    override suspend fun deleteJoinStatus(req: DeleteJoinStatusRequest): DeleteJoinStatusResponse {
        return postRequest(
            "/JoinStatus/Delete", req
        )
    }

    private fun getEndpointUrl(endpoint: String): String {
        customServerUrl?.let {
            return "${it}$endpoint"
        }
        return "${AppConfig.BACKEND_URL}$endpoint"
    }

    private suspend inline fun <reified TResponse : FamilyVaultBackendResponse> postRequest(endpoint: String): TResponse {
        return postRequest(endpoint, null)
    }

    private suspend inline fun <reified TResponse : FamilyVaultBackendResponse> postRequest(
        endpoint: String, req: FamilyVaultBackendRequest?
    ): TResponse {
        val response: HttpResponse

        try {
            response = client.post(getEndpointUrl(endpoint)) {
                contentType(ContentType.Application.Json)
                setBody(req)
            }
        } catch (e: ConnectTimeoutException) {
            throw FamilyVaultBackendNoConnectionException(e.message ?: "")
        }

        if (response.status.isSuccess()) {
            return response.body<TResponse>()
        } else {
            throw FamilyVaultBackendErrorException(
                response.status, response.bodyAsText()
            )
        }
    }

    private suspend inline fun <reified TResponse : FamilyVaultBackendResponse> getRequest(endpoint: String): TResponse {
        return getRequest(endpoint, null)
    }

    private suspend inline fun <reified TResponse : FamilyVaultBackendResponse> getRequest(
        endpoint: String, req: FamilyVaultBackendRequest?
    ): TResponse {
        val response: HttpResponse

        try {
            response = client.get(getEndpointUrl(endpoint)) {
                contentType(ContentType.Application.Json)
                req?.toParameters()?.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
        } catch (e: ConnectTimeoutException) {
            throw FamilyVaultBackendNoConnectionException(e.message ?: "")
        }

        if (response.status.isSuccess()) {
            return response.body<TResponse>()
        } else {
            throw FamilyVaultBackendErrorException(
                response.status, response.bodyAsText()
            )
        }
    }
}