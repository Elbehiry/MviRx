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
import com.elbehiry.shared.base.UseCase
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class RemoveOrderUseCase @Inject constructor() :
    UseCase<RemoveOrderUseCase.Params, Observable<OrdersListPartialState>>() {

    override fun execute(parameters: Params): Observable<OrdersListPartialState> {
        return Observable.fromIterable(parameters.ordersList)
            .filter { it.id != parameters.removedOrder.id }
            .toList()
            .toObservable()
            .map { mapOrders(it) }
            .subscribeOn(Schedulers.io())
    }

    private fun mapOrders(orders: List<OrdersItem>): OrdersListPartialState =
        OrdersListPartialState.Orders(orders)

    class Params private constructor(
        val removedOrder: OrdersItem,
        val ordersList: List<OrdersItem>
    ) {
        companion object {
            @JvmStatic
            fun create(removedOrder: OrdersItem, ordersList: List<OrdersItem>): Params {
                return Params(removedOrder, ordersList)
            }
        }
    }
}
