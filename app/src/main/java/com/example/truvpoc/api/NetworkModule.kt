package com.example.truvpoc.api

import com.example.truvpoc.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {

    private const val baseUrl = BuildConfig.truvApiUrl
    private const val clientId = BuildConfig.truvClientId
    private const val secret = BuildConfig.truvSecret

    val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(Interceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                .header("X-Access-Client-Id", clientId)
                .header("X-Access-Secret", secret)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .method(original.method, original.body)

            chain.proceed(request.build())
        })
        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val api = retrofit.create(TruvApi::class.java)

}