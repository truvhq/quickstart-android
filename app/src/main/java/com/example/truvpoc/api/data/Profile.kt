package com.example.truvpoc.api.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Profile(
    @Json(name = "first_name")
    val firstName: String?,
    @Json(name = "last_name")
    val lastName: String?,
    @Json(name = "ssn")
    val ssn: String?,
    @Json(name = "date_of_birth")
    val dateOfBirth: String?
)