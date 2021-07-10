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

package com.elbehiry.shared.network

import com.elbehiry.shared.data.remote.DinDinnApi
import com.elbehiry.test_shared.orders
import com.google.gson.GsonBuilder
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Before
import org.junit.Test
import org.junit.Assert
import org.junit.After
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DinDinnServiceTestUsingMockWebServer {

    @get:Rule
    val mockWebServer = MockWebServer()
    private lateinit var dinDinnService: DinDinnApi

    private val gson by lazy {
        GsonBuilder()
            .serializeNulls()
            .setLenient()
            .create()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Before
    fun setUp() {
        dinDinnService = retrofit.create(DinDinnApi::class.java)
    }

    @Test
    fun test_getOrders_should_success() {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(orders)
                .setResponseCode(200)
        )

        val ordersResult = dinDinnService.getOrders().blockingGet()
        mockWebServer.takeRequest()
        Assert.assertEquals(ordersResult.size, 1)
    }

    @After
    fun tearDown() {
        try {
            mockWebServer.shutdown()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}
