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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.elbehiry.dindinn.R
import com.elbehiry.dindinn.databinding.OrderItemLayout
import com.elbehiry.model.OrdersItem

class OrdersViewHolder(private val binding: OrderItemLayout) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrdersItem) {
        binding.item = item
        binding.addonAdapter = AddonsAdapter(item.addon)
    }

    companion object {

        private fun getItemLayoutId(): Int = R.layout.orders_list_item

        private fun inflateItemUsing(parent: ViewGroup): OrderItemLayout =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                getItemLayoutId(),
                parent,
                false
            )

        infix fun initializeWith(parent: ViewGroup): OrdersViewHolder {
            return OrdersViewHolder(inflateItemUsing(parent))
        }
    }
}
