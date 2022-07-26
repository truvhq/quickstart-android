package com.example.truvpoc.api.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Employment(
    @Json(name = "start_date")
    val startDate: String?,
    @Json(name = "end_date")
    val endDate: String?,
    @Json(name = "job_type")
    val jobType: String?,
    @Json(name = "job_title")
    val jobTitle: String?,
    @Json(name = "annual_salary")
    val annualSalary: String?,
    @Json(name = "profile")
    val profile: Profile,
    @Json(name = "company")
    val company: Company
)