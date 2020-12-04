package com.android.tinysquare.di

import com.android.tinysquare.BuildConfig
import com.android.tinysquare.data.source.remote.base.ApiService
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


const val BASE_URL = "https://api.foursquare.com/v2/"
const val KEY_CLIENT_ID = "client_id"
const val KEY_CLIENT_SECRET = "client_secret"
const val KEY_CLIENT_VERSION = "v"
const val CLIENT_ID = "LM42JXX4FD4KDAXO21E2LI0FRPGW2JJTTHLAQRXQVRIQGRY0"
const val CLIENT_SECRET = "ZWEMFU4YV5LGGPCYQQAG4MJSNJBB4CZUJCSL1X4L2EW20KRM"
const val CLIENT_V = "20202810"
const val TIME_OUT = 30L


val networkModule = module {

    single { createService(get()) }

    single { createRetrofit(get(), get()) }

    single { createOkHttpClient() }

    single { Moshi.Builder().build() }

}


fun createOkHttpClient(): OkHttpClient {
    val client = OkHttpClient.Builder()
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
    if (BuildConfig.DEBUG) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        client.addNetworkInterceptor(interceptor)
    }
    client.addInterceptor { chain ->
        val url = chain.request().url.newBuilder()
            .addQueryParameter(KEY_CLIENT_ID, CLIENT_ID)
            .addQueryParameter(KEY_CLIENT_SECRET, CLIENT_SECRET)
            .addQueryParameter(KEY_CLIENT_VERSION, CLIENT_V)
            .build()
        chain.proceed(
            chain.request()
                .newBuilder()
                .url(url)
                .build()
        )
    }.build()

    return client.build()
}


fun createRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()
}

fun createService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}



