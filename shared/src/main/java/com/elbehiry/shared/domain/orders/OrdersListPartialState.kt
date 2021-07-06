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

package com.elbehiry.shared.domain.orders

import com.elbehiry.model.OrdersItem
import com.elbehiry.shared.base.MVIPartialState

sealed class OrdersListPartialState : MVIPartialState<OrdersViewState> {
    abstract override fun reduce(
        oldVs: OrdersViewState,
        initialVs: OrdersViewState
    ): OrdersViewState

    object Empty : OrdersListPartialState() {
        override fun reduce(oldVs: OrdersViewState, initialVs: OrdersViewState): OrdersViewState {
            return initialVs.copy(empty = true)
        }
    }

    class Failure(private val throwable: Throwable) : OrdersListPartialState() {
        override fun reduce(oldVs: OrdersViewState, initialVs: OrdersViewState): OrdersViewState {
            return initialVs.copy(error = throwable)
        }
    }

    object Loading : OrdersListPartialState() {
        override fun reduce(oldVs: OrdersViewState, initialVs: OrdersViewState): OrdersViewState {
            return initialVs.copy(loading = true)
        }
    }

    class Orders(private val orders: List<OrdersItem>) : OrdersListPartialState() {
        override fun reduce(oldVs: OrdersViewState, initialVs: OrdersViewState): OrdersViewState {
            return initialVs.copy(orders = orders)
        }
    }
}
