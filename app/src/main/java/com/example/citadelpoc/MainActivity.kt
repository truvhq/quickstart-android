package com.example.citadelpoc

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private inner class JsInterface {

        @android.webkit.JavascriptInterface
        fun onSuccess(public_token: String) {
            Toast.makeText(applicationContext, "Public Token: $public_token", Toast.LENGTH_LONG).show()
        }

        fun onLoad() {
            Log.d("CitadelID", "Bridge Loaded")
        }

        fun onClose() {
            Log.d("CitadelID", "Bridge Closed")
        }

        fun onEvent(eventType: String, payload: JSONObject) {
            Log.d("CitadelID", "Event: $eventType")
        }
    }

    fun getBridgeToken() {
        val clientId = BuildConfig.citadelClientId
        val secret = BuildConfig.citadelSecret
        var url = BuildConfig.citadelApiUrl

        val request = object: JsonObjectRequest(Request.Method.POST, url, null,
                Response.Listener { response: JSONObject ->
                    Toast.makeText(applicationContext, response.getString("bridge_token"), Toast.LENGTH_LONG).show()
                    loadWidget(response.getString("bridge_token"))
                },
                Response.ErrorListener { error: VolleyError ->
                    Log.e("CITADEL", error.toString())
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_LONG).show()
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

        CitadelSingleton.getInstance(this.applicationContext).addToRequestQueue(request)
    }

    fun loadWidget(bridgeToken: String) {
        val myWebView: WebView = findViewById(R.id.webview)
        myWebView.settings.javaScriptEnabled = true
        myWebView.addJavascriptInterface(JsInterface(), "citadelInterface")
        myWebView.loadUrl("file:///android_asset/www/index.html?bridge_token=$bridgeToken")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getBridgeToken()
    }
}