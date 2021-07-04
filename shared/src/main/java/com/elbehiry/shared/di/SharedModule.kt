package com.elbehiry.shared.di

import com.elbehiry.shared.BuildConfig
import com.elbehiry.shared.data.network.CommonHeaderInterceptor
import com.elbehiry.shared.data.network.HttpLogger
import com.elbehiry.shared.data.remote.DinDinnApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SharedModule {

    @Provides
    fun provideLoggingInterceptor(logger: HttpLoggingInterceptor.Logger): HttpLoggingInterceptor =
        HttpLoggingInterceptor(logger).setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun provideOkHttp(
       httpLoggingInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10L, TimeUnit.SECONDS)
            .writeTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideCommonHeaderInterceptor(): Interceptor {
        return CommonHeaderInterceptor()
    }

    @Provides
    fun provideHttpClient(
        headersInterceptor: CommonHeaderInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10L, TimeUnit.SECONDS)
        .writeTimeout(10L, TimeUnit.SECONDS)
        .readTimeout(30L, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(headersInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().serializeNulls().create()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideDinDinnApi(retrofit: Retrofit): DinDinnApi =
        retrofit.create(DinDinnApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkingBinds() {
    @Binds
    abstract fun bindHttpLogger(
        httpLogger: HttpLogger,
    ): HttpLoggingInterceptor.Logger
}