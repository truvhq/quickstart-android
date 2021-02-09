# Introduction
Let's get you started with Citadel by walking through this Android Quickstart app. You'll need a set of API keys which you can get by signing up at https://dashboard.citadelid.com

You'll have two different API keys used by the back end, `client_id` and `access_key`.


# Set up the Android Quickstart
Once you have your API keys, it's time to run the Citadel Android Quickstart app locally.
*Requirements*: Android Studio

1. `git clone https://github.com/citadelid/quickstart-android`
2. `cd quickstart-android`
3. Create a `local.properties` file with the following content (values with <> should be replaced by the proper keys or values):
```
citadelClientId="<client_id>"
citadelSecret="<access_key>"
citadelApiUrl="https://prod.citadelid.com/v1/"
citadelProductType="<employment or income>"
```
4. Open the project in Android Studio and run the app

# Run your first verification
## Overview
The Android Quickstart app emulates the experience of an applicant going through a background check/income verification inside an android app.

If the verification is successful via Citadel, we return the data on screen. 

## Successful verification

After opening the Android Quickstart app you will be presented with the Citadel Bridge. Select the button to choose a Payroll Provider and choose "ADP"

Use the Sandbox credentials to simulate a successful login.

```
username: goodlogin
password: goodpassword
```

Once you have entered your credentials and moved to the next screen, you have succesfully done your first verification. 

The API call will be executed and the data will be loaded into the next view.

# What happened under the hood

- :smiley: = User
- :iphone: = Android App

Here is the flow that a successful verification process takes in our example:

1. [:iphone: sends API request to Citadel for `bridge_token`](#step-1)
2. [:iphone: loads mobile page from Citadel CDN with `bridge_token` into native WebView](#step-2)
3. [:smiley: selects employer, choses provider, logs in, clicks `Done`](#step-3)
4. [:iphone: sends API request to Citadel exchanging temporary `token` for `access_token`](#step-4)
5. [:iphone: sends API request to Citadel with `access_token` for employment/income verification](#step-5)
6. [:iphone: renders the verification info sent back by Citadel for :smiley: to view](#step-6)

## <a id="step-1"></a>1. :iphone: sends API request to Citadel for `bridge_token`
```
  citadel.getBridgeToken { token ->
    if(token != null) {
      loadWidget(token)
    } else {
      Toast.makeText(applicationContext,"Issue with Bridge Token", Toast.LENGTH_LONG).show()
    }
  }
```
```
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
```
## <a id="step-2"></a>2. :iphone: loads mobile page from Citadel CDN into native WebView
```
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
      .appendQueryParameter("product", BuildConfig.citadelProductType)
      .appendQueryParameter("tracking_info", "tracking_info")
      .appendQueryParameter("client", "Your company name")
      .fragment("section-name")
    myWebView.loadUrl(builder.build().toString())
  }
```
## <a id="step-3"></a>3. :smiley: selects employer, choses provider, logs in, clicks `Done`
## <a id="step-4"></a>4. :iphone: sends API request to Citadel exchanging temporary `token` for `access_token`
```
  @android.webkit.JavascriptInterface
  fun onSuccess(payloadJSON: String) {
    val payload = JSONObject(payloadJSON)
    val publicToken = payload.getString("public_token")
    citadel.getAccessToken(publicToken) { accessToken ->
    ...
```
```
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
```
## <a id="step-5"></a>5. :iphone: sends API request to Citadel with `access_token` for employment/income verification
```
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
```
## <a id="step-6"></a>6. :iphone: renders the verification info sent back by Citadel for :smiley: to view
```
  fun showEmploymentResults(verification: JSONObject) {
    val intent = Intent(this, DisplayEmploymentActivity::class.java).apply {
      putExtra("verification", verification.toString())
    }
    startActivity(intent)
  }
```