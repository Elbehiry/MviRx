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

import com.elbehiry.shared.data.orders.remote.IGetOrdersDataSource
import com.elbehiry.shared.data.orders.repository.GetOrdersRepository
import com.elbehiry.shared.data.orders.repository.IGetOrdersRepository
import com.elbehiry.shared.domain.orders.OrdersListPartialState
import com.elbehiry.test_shared.ORDERS_ITEMS
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetOrdersRepositoryTest {

    @MockK
    private lateinit var remoteDataSource: IGetOrdersDataSource
    private lateinit var repository: IGetOrdersRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = GetOrdersRepository(remoteDataSource)
    }

    @Test
    fun `get orders from repository call api remote data source get orders`() {
        every { remoteDataSource.getOrders() } returns Single.just(ORDERS_ITEMS)
        val testObserver = repository.getOrders().test()
        verify { remoteDataSource.getOrders() }
        testObserver.assertNoErrors().assertComplete().dispose()
    }

    @Test
    fun `get orders from repository returns order view state`() {
        every { remoteDataSource.getOrders() } returns Single.just(ORDERS_ITEMS)
        val partialState = repository.getOrders().blockingGet()
        Assert.assertTrue(partialState is OrdersListPartialState.Orders)
        verify { remoteDataSource.getOrders() }
    }

    @Test
    fun `get empty orders returns empty view state`() {
        every { remoteDataSource.getOrders() } returns Single.just(emptyList())
        val partialState = repository.getOrders().blockingGet()
        Assert.assertTrue(partialState is OrdersListPartialState.Empty)
        verify { remoteDataSource.getOrders() }
    }

    @Test
    fun `get orders with error from repository returns error view state`() {
        val errorMessage = "Network Exception"
        every { remoteDataSource.getOrders() } returns Single.error(Exception(errorMessage))
        val partialState = repository.getOrders().blockingGet()
        Assert.assertTrue(partialState is OrdersListPartialState.Failure)
        verify { remoteDataSource.getOrders() }
    }
}
