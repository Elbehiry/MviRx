package com.elbehiry.dindinn.orders.presentation.viewmodel

import com.elbehiry.shared.base.MVIAction

sealed class OrdersListActions : MVIAction {
    object GetOrders : OrdersListActions()
}