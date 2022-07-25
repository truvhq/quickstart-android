package com.example.truvpoc.api.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Address(
    val city: String?,
    val state: String?
)