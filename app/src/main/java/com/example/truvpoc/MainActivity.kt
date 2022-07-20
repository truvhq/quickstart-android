package com.example.truvpoc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.truv.TruvBridgeView
import com.truv.TruvEventsListener
import com.truv.models.TruvEventPayload
import com.truv.models.TruvSuccessPayload
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var truv: Truv
    private lateinit var myWebView: TruvBridgeView

    private val truvEventsListener = object : TruvEventsListener {

        override fun onClose() {
            Log.d(TAG, "Bridge Closed")
        }

        override fun onError() {
            Log.e(TAG, "Bridge Error")
        }

        override fun onEvent(event: TruvEventPayload.EventType) {
            Log.d(TAG, "Event: $event")
        }

        override fun onLoad() {
            Log.d(TAG, "Bridge Loaded")
        }

        override fun onSuccess(payload: TruvSuccessPayload) {
            Log.d(TAG, "Bridge Success")

            truv.getAccessToken(payload.publicToken) { accessToken ->
                if(accessToken != null) {
                    if(BuildConfig.truvProductType == "employment") {
                        getEmploymentVerification(accessToken)
                    } else {
                        getIncomeVerification(accessToken)
                    }
                }
            }
        }

    }

    fun getEmploymentVerification(accessToken: String) {
        truv.getEmploymentInfoByToken(accessToken) { employmentInfo ->
            if(employmentInfo != null) {
                showEmploymentResults(employmentInfo)
            }
        }
    }

    fun getIncomeVerification(accessToken: String) {
        truv.getIncomeInfoByToken(accessToken) { incomeInfo ->
            if(incomeInfo != null) {
                showIncomeResults(incomeInfo)
            }
        }
    }

    private fun showEmploymentResults(verification: JSONObject) {
        val intent = Intent(this, DisplayEmploymentActivity::class.java).apply {
            putExtra("verification", verification.toString())
        }
        startActivity(intent)
    }

    private fun showIncomeResults(verification: JSONObject) {
        val intent = Intent(this, DisplayIncomeActivity::class.java).apply {
            putExtra("verification", verification.toString())
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        truv = Truv(applicationContext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myWebView = findViewById(R.id.bridgeView)
        myWebView.addEventListener(truvEventsListener)

        truv.getBridgeToken { token ->
            if(token != null) {
                myWebView.loadBridgeTokenUrl(token)
            } else {
                Toast.makeText(applicationContext,"Issue with Bridge Token", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val TAG = "Truv"
    }

}