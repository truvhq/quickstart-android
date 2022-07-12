package com.example.truvpoc

import com.android.volley.Request
import android.content.Context
import org.json.JSONObject

class Truv(context: Context) {

    private val appContext = context

    public fun getBridgeToken(callback: (String?) -> Unit) {
        var endpoint = "bridge-tokens/"
        var body = JSONObject()
        body.put("product_type", BuildConfig.truvProductType)
        VolleySingleton.getInstance(appContext).addToRequestQueue(Request.Method.POST, endpoint, body) { response ->
            if(response != null) {
                callback(response.getString("bridge_token"))
            } else {
                callback(null)
            }
        }
    }

    public fun getAccessToken(publicToken: String, callback: (String?) -> Unit) {
        var endpoint = "link-access-tokens/"
        var body = JSONObject()
        body.put("public_token", publicToken)

        VolleySingleton.getInstance(appContext).addToRequestQueue(Request.Method.POST, endpoint, body) { response ->
            if(response != null) {
                callback(response.getString("access_token"))
            } else {
                callback(null)
            }
        }
    }

    public fun getEmploymentInfoByToken(accessToken: String, callback: (JSONObject?) -> Unit) {
        var endpoint = "verifications/employments/"
        var body = JSONObject()
        body.put("access_token", accessToken)

        VolleySingleton.getInstance(appContext).addToRequestQueue(Request.Method.POST, endpoint, body) { response ->
            if(response != null) {
                callback(response)
            } else {
                callback(null)
            }
        }
    }

    public fun getIncomeInfoByToken(accessToken: String, callback: (JSONObject?) -> Unit) {
        var endpoint = "verifications/incomes/"
        var body = JSONObject()
        body.put("access_token", accessToken)

        VolleySingleton.getInstance(appContext).addToRequestQueue(Request.Method.POST, endpoint, body) { response ->
            if(response != null) {
                callback(response)
            } else {
                callback(null)
            }
        }
    }
}