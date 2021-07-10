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
import com.elbehiry.shared.data.orders.repository.IGetOrdersRepository
import com.elbehiry.shared.domain.orders.GetOrdersUseCase
import com.elbehiry.shared.domain.orders.OrdersViewState
import com.elbehiry.shared.domain.orders.RemoveOrderUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class OrderListViewModelTest {
    @MockK
    private lateinit var getOrdersUseCase: GetOrdersUseCase

    @MockK
    private lateinit var removeOrderUseCase: RemoveOrderUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var ordersRepository: IGetOrdersRepository
    private lateinit var orderListViewModel: OrderListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        removeOrderUseCase = mockk()
        savedStateHandle = mockk()
        orderListViewModel = OrderListViewModel(
            savedStateHandle, getOrdersUseCase, removeOrderUseCase
        )
    }

    @Test
    fun `should emit initial state without initial query when initial query empty`() {
        orderListViewModel.states().test().assertValue(OrdersViewState())
    }

//    @Test
//    fun `should start with loading`() {
//        every { ordersRepository.getOrders() } returns Single.just(
//            OrdersListPartialState.Orders(
//                ORDERS_ITEMS
//            )
//        )
//        orderListViewModel.processIntents(Observable.just(OrdersListActions.GetOrders))
//        orderListViewModel.states().test().assertValue(OrdersViewState(loading = true))
//    }
}
