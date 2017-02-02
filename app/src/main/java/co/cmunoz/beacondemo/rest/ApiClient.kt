package com.hugeinc.sxsw.rest

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Api Client
 *
 * User: cmunoz
 * Date: 1/20/17
 * Time: 11:50 AM
 */
class ApiClient {

  companion object{
    val BASE_URL: String = "http://demo1500759.mockable.io/" //a mock web service base URL
  }

  fun build(): Retrofit {
    val okHttpClient = OkHttpClient.Builder().addNetworkInterceptor(StethoInterceptor()).build()
    return Retrofit.Builder().client(okHttpClient).baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
  }

}