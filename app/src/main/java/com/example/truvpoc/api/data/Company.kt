package com.example.truvpoc.api.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Company(
    val name: String?,
    val phone: String?,
    val address: Address?
)