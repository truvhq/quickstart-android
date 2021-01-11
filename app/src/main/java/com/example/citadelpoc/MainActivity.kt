package com.example.citadelpoc

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private inner class JsInterface {

        @android.webkit.JavascriptInterface
        fun onSuccess(payload: JSONObject) {
            Toast.makeText(applicationContext, "Successful verification. Public Token: $payload.public_token", Toast.LENGTH_LONG).show()
        }

        fun onLoad() {
            Log.d("CitadelID", "Bridge Loaded")
        }

        fun onClose() {
            Log.d("CitadelID", "Bridge Closed")
            Toast.makeText(applicationContext, "Verification wasn't finished", Toast.LENGTH_LONG).show()
        }

        fun onEvent(eventType: String, payload: JSONObject) {
            Log.d("CitadelID", "Event: $eventType")
        }
    }

    fun getBridgeToken() {
        val clientId = BuildConfig.citadelClientId
        val secret = BuildConfig.citadelSecret
        var url = "${BuildConfig.citadelApiUrl}bridge-tokens/"

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
        myWebView.clearCache(true)
        myWebView.settings.javaScriptEnabled = true
        myWebView.addJavascriptInterface(JsInterface(), "citadelInterface")
        val builder: Uri.Builder = Uri.Builder()
        builder.scheme("https")
            .authority("cdn-dev.citadelid.com")
            .appendPath("mobile.html")
            .appendQueryParameter("bridge_token", bridgeToken)
            .appendQueryParameter("product", "employment")
            .appendQueryParameter("tracking_info", "tracking_info")
            .appendQueryParameter("client", "Your company name")
            .fragment("section-name")
        Log.d("CITADEL", builder.build().toString())
        myWebView.loadUrl(builder.build().toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getBridgeToken()
    }
}