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

package com.elbehiry.dindinn.orders.presentation

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.recyclerview.widget.RecyclerView
import com.elbehiry.dindinn.orders.presentation.adapter.OrdersAdapter
import com.elbehiry.shared.domain.orders.OrdersViewState
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

@BindingAdapter("ordersViewState", requireAll = false)
fun RecyclerView.setRecipesState(oldState: OrdersViewState?, viewState: OrdersViewState?) {
    if (oldState == viewState) return
    viewState?.let {
        handleAdapterStates(it)
    }
}

private fun RecyclerView.handleAdapterStates(viewState: OrdersViewState) {
    if (viewState.orders.isNotEmpty())
        setRecipesToAdapter(viewState)
}

private fun RecyclerView.setRecipesToAdapter(viewState: OrdersViewState) {
    adapter?.let {
        (it as OrdersAdapter).apply {
            if (viewState.orders != this.orders) {
                this.setData(viewState.orders)
            }
        }
    }
}

@BindingConversion
fun visibility(visible: Boolean) = if (visible) View.VISIBLE else View.GONE

@BindingConversion
fun visibility(visible: Throwable?) = if (visible == null) View.GONE else View.VISIBLE

@BindingAdapter("formatOrderDate")
fun TextView.formatOrderDate(inputDate: String?) {
    if (inputDate != null) {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ssZ"
        val resultPattern = "hh:mm a"
        val formattedDate = SimpleDateFormat(inputPattern, Locale.ENGLISH)
            .parse(inputDate)
        requireNotNull(formattedDate)
        val outputFormatter: DateFormat =
            SimpleDateFormat(resultPattern, Locale.getDefault())
        this.text = outputFormatter.format(formattedDate) ?: ""
    }
}
