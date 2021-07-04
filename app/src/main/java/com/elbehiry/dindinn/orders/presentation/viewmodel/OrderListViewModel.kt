package com.elbehiry.dindinn.orders.presentation.viewmodel

import com.elbehiry.shared.base.BaseVM
import com.elbehiry.shared.domain.orders.GetOrdersUseCase
import com.elbehiry.shared.domain.orders.OrdersListPartialState
import com.elbehiry.shared.domain.orders.OrdersViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase
) : BaseVM<OrdersListActions, OrdersViewState, OrdersListPartialState>() {

    override val initialState by lazy { OrdersViewState() }

    override fun reduce(
        result: OrdersListPartialState,
        previousState: OrdersViewState
    ): OrdersViewState {
        return result.reduce(previousState, initialState)
    }

    private val initial by lazy {
        ObservableTransformer<OrdersListActions.GetOrders, OrdersListPartialState> { actions ->
            actions.flatMap {
                getOrdersUseCase(Unit)
            }
        }
    }

    override fun handle(
        action: Observable<OrdersListActions>
    ): List<Observable<out OrdersListPartialState>> =
        listOf(
            action.ofType(OrdersListActions.GetOrders::class.java)
                .take(1)
                .compose(initial)
        )
}