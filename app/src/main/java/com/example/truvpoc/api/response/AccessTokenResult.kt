package com.example.truvpoc.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccessTokenResult(
    @Json(name = "access_token")
    val accessToken: String
)
