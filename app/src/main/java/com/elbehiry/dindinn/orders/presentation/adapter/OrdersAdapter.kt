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

package com.elbehiry.dindinn.orders.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elbehiry.dindinn.utils.propagateTo
import com.elbehiry.model.OrdersItem
import kotlin.properties.Delegates

class OrdersAdapter(private val actionHandler: IActionHandler) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var orders by Delegates.observable(mutableListOf<OrdersItem>()) { _, old, new ->
        propagateTo(old) {
            newItems = new
            compareItems = { old, new -> old.id == new.id }
        }
    }

    fun setData(newList: List<OrdersItem>) {
        orders = newList.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrdersViewHolder initializeWith parent
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OrdersViewHolder)
            holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size
}
