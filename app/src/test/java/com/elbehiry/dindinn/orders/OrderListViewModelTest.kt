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

package com.elbehiry.dindinn.orders

import androidx.lifecycle.SavedStateHandle
import com.elbehiry.dindinn.orders.presentation.viewmodel.OrderListViewModel
import com.elbehiry.dindinn.orders.presentation.viewmodel.OrdersListActions
import com.elbehiry.dindinn.orders.presentation.viewmodel.ordersKey
import com.elbehiry.model.OrdersItem
import com.elbehiry.shared.domain.orders.GetOrdersUseCase
import com.elbehiry.shared.domain.orders.OrdersListPartialState
import com.elbehiry.shared.domain.orders.OrdersViewState
import com.elbehiry.shared.domain.orders.RemoveOrderUseCase
import com.elbehiry.test_shared.ORDERS_ITEMS
import com.elbehiry.test_shared.ORDER_ITEM
import com.elbehiry.test_shared.faker
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Test

class OrderListViewModelTest {
    @MockK
    private lateinit var getOrdersUseCase: GetOrdersUseCase

    @MockK
    private lateinit var removeOrderUseCase: RemoveOrderUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var orderListViewModel: OrderListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        removeOrderUseCase = mockk()
        savedStateHandle = mockk()
        every { savedStateHandle.set<List<OrdersItem>?>(ordersKey, any()) } returns Unit
        orderListViewModel = OrderListViewModel(
            savedStateHandle, getOrdersUseCase, removeOrderUseCase
        )
    }

    @Test
    fun `should emit initial state without initial query when initial query empty`() {
        orderListViewModel.states().test().assertValue(OrdersViewState())
    }

    @Test
    fun `should start with loading`() {
        every { savedStateHandle.get<List<OrdersItem>?>(ordersKey) } returns null
        every { getOrdersUseCase(Unit) } returns Observable.just(
            OrdersListPartialState.Loading
        )
        val testObserver = orderListViewModel.states().test()
        orderListViewModel.processIntents(Observable.just(OrdersListActions.GetOrders))
        testObserver.assertValueAt(1) { it.loading }
    }

    @Test
    fun `loading should not appear if there is data inside the saved state`() {
        every { savedStateHandle.get<List<OrdersItem>?>(ordersKey) } returns ORDERS_ITEMS
        val testObserver = orderListViewModel.states().test()
        orderListViewModel.processIntents(Observable.just(OrdersListActions.GetOrders))
        testObserver.assertValueAt(1) { !it.loading }
    }

    @Test
    fun `should return data from saved state if there is data inside the saved state`() {
        every { savedStateHandle.get<List<OrdersItem>?>(ordersKey) } returns ORDERS_ITEMS
        val testObserver = orderListViewModel.states().test()
        orderListViewModel.processIntents(Observable.just(OrdersListActions.GetOrders))
        testObserver.assertValueAt(1) { it.orders == ORDERS_ITEMS }
    }

    @Test
    fun `remove item should remove it from list successfully`() {
        val item = ORDER_ITEM.copy(
            id = faker.number().digits(3).toInt(),
        )
        every { savedStateHandle.get<List<OrdersItem>?>(ordersKey) } returns listOf(item)
        val testObserver = orderListViewModel.states().test()
        orderListViewModel.processIntents(Observable.just(OrdersListActions.RemoveOrder(item)))
        testObserver.assertValueAt(0) { it.orders.isEmpty() }
    }

    @Test
    fun `refresh data start with loading`() {
        every { savedStateHandle.get<List<OrdersItem>?>(ordersKey) } returns null
        every { getOrdersUseCase(Unit) } returns Observable.just(
            OrdersListPartialState.Loading
        )
        val testObserver = orderListViewModel.states().test()
        orderListViewModel.processIntents(Observable.just(OrdersListActions.Refresh))
        testObserver.assertValueAt(1) { it.loading }
    }

    @Test
    fun `refresh data should return the right state`() {
        every { savedStateHandle.get<List<OrdersItem>?>(ordersKey) } returns null
        every { getOrdersUseCase(Unit) } returns Observable.just(
            OrdersListPartialState.Orders(ORDERS_ITEMS)
        )
        val testObserver = orderListViewModel.states().test()
        orderListViewModel.processIntents(Observable.just(OrdersListActions.Refresh))
        testObserver.assertValueAt(1) { it.orders == ORDERS_ITEMS }
    }
}
