package com.elbehiry.dindinn.orders.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elbehiry.dindinn.databinding.OrdersListFragmentView
import com.elbehiry.dindinn.orders.presentation.viewmodel.OrderListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderListFragment  :Fragment() {

    lateinit var binding : OrdersListFragmentView
    private val ordersViewModel : OrderListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        with(OrdersListFragmentView.inflate(layoutInflater, container, false)) {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            return root
        }
    }
}