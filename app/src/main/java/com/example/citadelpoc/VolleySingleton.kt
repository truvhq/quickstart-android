package com.example.citadelpoc

import android.content.Context
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class VolleySingleton constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: VolleySingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VolleySingleton(context).also {
                    INSTANCE = it
                }
            }
    }

    var baseUrl = BuildConfig.citadelApiUrl

    val requestQueue: RequestQueue = run {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }

    fun addToRequestQueue(method: Int, endpoint: String, body: JSONObject?, callback: (JSONObject?) -> Unit) {
        val clientId = BuildConfig.citadelClientId
        val secret = BuildConfig.citadelSecret
        var url = "${baseUrl}${endpoint}"

        val req = object: JsonObjectRequest(method, url, body,
                Response.Listener { response: JSONObject ->
                    callback(response)
                },
                Response.ErrorListener { error: VolleyError ->
                    Log.e("CitadelID", "ERROR with $url")
                    Log.e("CitadelID", error.message.toString())
                    callback(null)
                }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["X-Access-Client-Id"] = clientId
                headers["X-Access-Secret"] = secret
                headers["Content-Type"] = "application/json"
                headers["Accept"] = "application/json"
                return headers
            }
        }
        requestQueue.add(req)
    }
}