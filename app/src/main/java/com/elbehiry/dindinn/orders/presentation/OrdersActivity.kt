package com.elbehiry.dindinn.orders.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elbehiry.dindinn.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
    }
}