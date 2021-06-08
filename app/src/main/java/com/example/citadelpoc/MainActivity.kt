package com.example.citadelpoc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    lateinit var citadel: Citadel

    private inner class JsInterface {

        @android.webkit.JavascriptInterface
        fun onSuccess(payloadJSON: String) {
            val payload = JSONObject(payloadJSON)
            val publicToken = payload.getString("public_token")
            citadel.getAccessToken(publicToken) { accessToken ->
                if(accessToken != null) {
                    if(BuildConfig.citadelProductType == "employment") {
                        getEmploymentVerification(accessToken)
                    } else {
                        getIncomeVerification(accessToken)
                    }
                }
            }
        }

        @android.webkit.JavascriptInterface
        fun onLoad() {
            Log.d("CitadelID", "Bridge Loaded")
        }

        @android.webkit.JavascriptInterface
        fun onClose() {
            Log.d("CitadelID", "Bridge Closed")
        }

        @android.webkit.JavascriptInterface
        fun onEvent(payloadJSON: String) {
            val payload = JSONObject(payloadJSON)
            val eventType = payload.getString("event_type")
            Log.d("CitadelID", "Event: $eventType")
        }
    }

    fun getEmploymentVerification(accessToken: String) {
        citadel.getEmploymentInfoByToken(accessToken) { employmentInfo ->
            if(employmentInfo != null) {
                showEmploymentResults(employmentInfo)
            }
        }
    }

    fun getIncomeVerification(accessToken: String) {
        citadel.getIncomeInfoByToken(accessToken) { incomeInfo ->
            if(incomeInfo != null) {
                showIncomeResults(incomeInfo)
            }
        }
    }

    fun loadWidget(bridgeToken: String) {
        val myWebView: WebView = findViewById(R.id.webview)
        myWebView.clearCache(true)
        myWebView.settings.javaScriptEnabled = true
        myWebView.addJavascriptInterface(JsInterface(), "citadelInterface")
        val builder: Uri.Builder = Uri.Builder()
        builder.scheme("https")
            .authority("cdn.citadelid.com")
            .appendPath("mobile.html")
            .appendQueryParameter("bridge_token", bridgeToken)
            .fragment("section-name")
        myWebView.loadUrl(builder.build().toString())
    }

    fun showEmploymentResults(verification: JSONObject) {
        val intent = Intent(this, DisplayEmploymentActivity::class.java).apply {
            putExtra("verification", verification.toString())
        }
        startActivity(intent)
    }

    fun showIncomeResults(verification: JSONObject) {
        val intent = Intent(this, DisplayIncomeActivity::class.java).apply {
            putExtra("verification", verification.toString())
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        citadel = Citadel(applicationContext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        citadel.getBridgeToken { token ->
            if(token != null) {
                loadWidget(token)
            } else {
                Toast.makeText(applicationContext,"Issue with Bridge Token", Toast.LENGTH_LONG).show()
            }
        }
    }
}