package com.example.truvpoc

import com.android.volley.Request
import android.content.Context
import org.json.JSONObject

class Truv(private val appContext: Context) {

    fun getBridgeToken(callback: (String?) -> Unit) {
        val endpoint = "bridge-tokens/"
        val body = JSONObject()
        body.put("product_type", BuildConfig.truvProductType)
        VolleySingleton.getInstance(appContext)
            .addToRequestQueue(Request.Method.POST, endpoint, body) { response ->
                if (response != null) {
                    callback(response.getString("bridge_token"))
                } else {
                    callback(null)
                }
            }
    }

    fun getAccessToken(publicToken: String, callback: (String?) -> Unit) {
        val endpoint = "link-access-tokens/"
        val body = JSONObject()
        body.put("public_token", publicToken)

        VolleySingleton.getInstance(appContext)
            .addToRequestQueue(Request.Method.POST, endpoint, body) { response ->
                if (response != null) {
                    callback(response.getString("access_token"))
                } else {
                    callback(null)
                }
            }
    }

    fun getEmploymentInfoByToken(accessToken: String, callback: (JSONObject?) -> Unit) {
        val endpoint = "verifications/employments/"
        val body = JSONObject()
        body.put("access_token", accessToken)

        VolleySingleton.getInstance(appContext)
            .addToRequestQueue(Request.Method.POST, endpoint, body) { response ->
                if (response != null) {
                    callback(response)
                } else {
                    callback(null)
                }
            }
    }

    fun getIncomeInfoByToken(accessToken: String, callback: (JSONObject?) -> Unit) {
        val endpoint = "verifications/incomes/"
        val body = JSONObject()
        body.put("access_token", accessToken)

        VolleySingleton.getInstance(appContext)
            .addToRequestQueue(Request.Method.POST, endpoint, body) { response ->
                if (response != null) {
                    callback(response)
                } else {
                    callback(null)
                }
            }
    }

}