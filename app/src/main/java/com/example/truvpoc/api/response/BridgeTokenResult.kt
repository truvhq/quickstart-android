package com.example.truvpoc.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BridgeTokenResult(
    @Json(name = "bridge_token")
    val bridgeToken: String
)
