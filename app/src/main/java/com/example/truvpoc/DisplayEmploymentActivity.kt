package com.example.truvpoc

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class DisplayEmploymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_employment)

        if (intent.hasExtra("verification")) {
            val verification = JSONObject(intent.getStringExtra("verification"))
            var employment = verification.getJSONArray("employments").getJSONObject(0)
            setFormValues(employment)
        }
    }

    fun setFormValues(employment: JSONObject) {
        var profile = employment.getJSONObject("profile")
        var company = employment.getJSONObject("company")

        setEditValue(R.id.textFirstName, profile.getString("first_name"))
        setEditValue(R.id.textLastName, profile.getString("last_name"))
        setEditValue(R.id.textSSN, profile.getString("ssn"))
        setEditValue(R.id.textDateOfBirth, profile.getString("date_of_birth"))

        setTextValue(R.id.textEmployerName, company.getString("name"))
        setEditValue(R.id.textEmployerCity, company.getJSONObject("address").getString("city"))
        setEditValue(R.id.textEmployerState, company.getJSONObject("address").getString("state"))
        setEditValue(R.id.textEmployerPhone, company.getString("phone"))

        setEditValue(R.id.textStartDate, employment.getString("start_date"))
        setEditValue(R.id.textEndDate, employment.getString("end_date"))
        setEditValue(R.id.textPositionType, employment.getString("job_type"))
        setEditValue(R.id.textTitle, employment.getString("job_title"))
    }

    fun setEditValue(element: Int, value: String) {
        val editText = findViewById<EditText>(element)
        editText.setText(value)
    }

    fun setTextValue(element: Int, value: String) {
        val editText = findViewById<TextView>(element).apply {
            text = value
        }
    }
}