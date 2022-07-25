package com.example.truvpoc.api.response

import com.example.truvpoc.api.data.Employment
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VerificationInfoResponse(
    val employments: List<Employment>
) {

    fun getEmployment() = employments.firstOrNull()

}
