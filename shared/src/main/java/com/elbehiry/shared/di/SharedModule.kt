/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkingBinds() {
    @Binds
    abstract fun bindHttpLogger(
        httpLogger: HttpLogger,
    ): HttpLoggingInterceptor.Logger
}

@InstallIn(SingletonComponent::class)
@Module
class SharedModule {

    @Provides
    @Singleton
    fun provideCommonHeaderInterceptor(): CommonHeaderInterceptor {
        return CommonHeaderInterceptor()
    }

    @Provides
    fun provideHttpClient(
        headersInterceptor: CommonHeaderInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10L, TimeUnit.SECONDS)
        .writeTimeout(10L, TimeUnit.SECONDS)
        .readTimeout(30L, TimeUnit.SECONDS)
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