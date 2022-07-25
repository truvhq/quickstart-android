package com.example.truvpoc.api

import com.example.truvpoc.api.response.AccessTokenResult
import com.example.truvpoc.api.response.BridgeTokenResult
import com.example.truvpoc.api.response.VerificationInfoResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TruvApi {

    @FormUrlEncoded
    @POST("bridge-tokens")
    suspend fun getBridgeToken(
        @Field("product_type") productType: String
    ): BridgeTokenResult

    @FormUrlEncoded
    @POST("link-access-tokens")
    suspend fun getAccessToken(
        @Field("public_token") publicToken: String
    ): AccessTokenResult

    @FormUrlEncoded
    @POST("verifications/employments")
    suspend fun getEmploymentInfoByToken(
        @Field("access_token") accessToken: String
    ): VerificationInfoResponse

    @FormUrlEncoded
    @POST("verifications/incomes")
    suspend fun getIncomeInfoByToken(
        @Field("access_token") accessToken: String
    ): VerificationInfoResponse

}