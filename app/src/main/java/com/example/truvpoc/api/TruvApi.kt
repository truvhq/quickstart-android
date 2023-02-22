package com.example.truvpoc.api

import com.example.truvpoc.api.response.AccessTokenResult
import com.example.truvpoc.api.response.BridgeTokenResult
import com.example.truvpoc.api.response.CreateUserResult
import com.example.truvpoc.api.response.VerificationInfoResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface TruvApi {

    @FormUrlEncoded
    @POST("users/")
    suspend fun createUser(
        @Field("external_user_id") externalUserId: String
    ): CreateUserResult

    @FormUrlEncoded
    @POST("users/{user_id}/tokens/")
    suspend fun getBridgeToken(
            @Path("user_id") userId: String,
            @Field("product_type") productType: String
    ): BridgeTokenResult

    @FormUrlEncoded
    @POST("link-access-tokens")
    suspend fun getAccessToken(
        @Field("public_token") publicToken: String
    ): AccessTokenResult

    @FormUrlEncoded
    @POST("links/reports/employment/")
    suspend fun getEmploymentInfoByToken(
        @Field("access_token") accessToken: String
    ): VerificationInfoResponse

    @FormUrlEncoded
    @POST("links/reports/income/")
    suspend fun getIncomeInfoByToken(
        @Field("access_token") accessToken: String
    ): VerificationInfoResponse

}