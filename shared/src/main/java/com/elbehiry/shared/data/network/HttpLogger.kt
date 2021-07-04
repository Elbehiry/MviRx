package com.elbehiry.shared.data.network

import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Inject

class HttpLogger @Inject constructor(): HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        Timber.d(message)
    }
}