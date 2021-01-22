# Citadel Android Proof of Concept

This is the POC for using Citadel natively in Android. This takes advantage of the android WebView
component. In order to get this working you need to create a `local.properties` file at the root
of the project with the following content:

```
citadelClientId="<Client ID>"
citadelSecret="<Client Secret>"
citadelApiUrl="https://prod.citadelid.com/v1/"
citadelProductType="employment"
```

Then run in Android Studio.