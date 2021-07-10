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

package com.elbehiry.shared.data.orders

import com.elbehiry.shared.data.orders.remote.GetOrdersRemoteDataSource
import com.elbehiry.shared.data.orders.remote.IGetOrdersDataSource
import com.elbehiry.shared.data.remote.DinDinnApi
import com.elbehiry.test_shared.ORDERS_ITEMS
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test

class OrdersRemoteDataSourceTest {

    @MockK
    private lateinit var api: DinDinnApi

    private lateinit var ordersRemoteDataSource: IGetOrdersDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        ordersRemoteDataSource = GetOrdersRemoteDataSource(api)
    }

    @Test
    fun `get orders from data source call api get orders`() {
        every { api.getOrders() } returns Single.just(ORDERS_ITEMS)
        val testObserver = ordersRemoteDataSource.getOrders().test()
        testObserver.assertResult(ORDERS_ITEMS)
        verify { api.getOrders() }
        testObserver.assertNoErrors().assertComplete().dispose()
    }

    @Test
    fun `given network error occurred, should return Single with error`() {
        val errorMessage = "Network Exception"
        every { api.getOrders() } returns Single.error(Exception(errorMessage))
        val testObserver = ordersRemoteDataSource.getOrders().test()
        testObserver.assertError {
            it.message == errorMessage
        }
        verify { ordersRemoteDataSource.getOrders() }
        testObserver.dispose()
    }
}
