package com.example.citadelpoc

import com.android.volley.Request
import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class Citadel(context: Context) {

    private val appContext = context

    public fun getBridgeToken(callback: (String?) -> Unit) {
        var endpoint = "bridge-tokens/"
        VolleySingleton.getInstance(appContext).addToRequestQueue(Request.Method.POST, endpoint, null) { response ->
            if(response != null) {
                callback(response.getString("bridge_token"))
            } else {
                callback(null)
            }
        }
    }

    public fun getAccessToken(publicToken: String, callback: (String?) -> Unit) {
        var endpoint = "access-tokens/"
        var body = JSONObject()
        var publicTokensArray = JSONArray()
        publicTokensArray.put(publicToken)
        body.put("public_tokens", publicTokensArray)

        VolleySingleton.getInstance(appContext).addToRequestQueue(Request.Method.POST, endpoint, body) { response ->
            if(response != null) {
                var responseArray = response.getJSONArray("access_tokens")
                callback(responseArray.getString(0))
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
}