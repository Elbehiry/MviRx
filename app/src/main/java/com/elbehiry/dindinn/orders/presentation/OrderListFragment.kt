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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.elbehiry.dindinn.R
import com.elbehiry.dindinn.databinding.OrdersListFragmentView
import com.elbehiry.dindinn.orders.presentation.adapter.IActionHandler
import com.elbehiry.dindinn.orders.presentation.adapter.OrdersAdapter
import com.elbehiry.dindinn.orders.presentation.viewmodel.OrderListViewModel
import com.elbehiry.dindinn.orders.presentation.viewmodel.OrdersListActions
import com.elbehiry.dindinn.utils.rxRefreshes
import com.elbehiry.shared.domain.orders.OrdersListPartialState
import com.elbehiry.shared.domain.orders.OrdersViewState
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber

@AndroidEntryPoint
class OrderListFragment : Fragment(), IActionHandler {

    lateinit var binding: OrdersListFragmentView
    private val ordersViewModel: OrderListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        with(OrdersListFragmentView.inflate(layoutInflater, container, false)) {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            actionHandler = this@OrderListFragment
            adapter = OrdersAdapter(this@OrderListFragment)
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ordersViewModel.partialStatPublisher
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::render) {
            }

        ordersViewModel.states()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::render) { Timber.e(it) }

        intents().apply(ordersViewModel::processIntents)
    }

    private fun render(vs: OrdersViewState) {
        binding.viewState = vs
    }

    private fun render(partialState: OrdersListPartialState) {
    }

    override fun retry() {
    }

    override fun onIngredients() {
        findNavController().navigate(R.id.action_orderListFragment_to_ingredientsFragment)
    }

    private fun intents() = Observable.merge(
        Observable.just(OrdersListActions.GetOrders),
        binding.swipeRefreshLayout.rxRefreshes().map { OrdersListActions.Refresh }
    )
}
