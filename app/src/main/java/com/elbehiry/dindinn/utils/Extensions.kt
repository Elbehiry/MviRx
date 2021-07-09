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

package com.elbehiry.dindinn.utils

import android.view.View
import androidx.annotation.CheckResult
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.rxjava3.core.Observable
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import java.util.TimeZone
import java.util.concurrent.TimeUnit

@CheckResult
fun SwipeRefreshLayout.rxRefreshes(): Observable<Unit> {
    return Observable.create<Unit> {
        setOnRefreshListener {
            isRefreshing = false
            it.onNext(Unit)
        }
    }.doOnComplete {
        setOnRefreshListener(null)
    }
}

fun View.isRtl() = layoutDirection == View.LAYOUT_DIRECTION_RTL

fun String.parseToDate(inputPattern: String): Date? {
    return SimpleDateFormat(inputPattern, Locale.ENGLISH)
        .apply { TimeZone.getTimeZone("GMT") }
        .parse(this)
}

fun Long.millisToSeconds(): Long = this / 1000

fun Long.formatPassedTimeToString(): String {
    val hours: Long = (TimeUnit.MILLISECONDS.toHours(this) % 24)
    val seconds: Long = (TimeUnit.MILLISECONDS.toSeconds(this) % 60)
    val minutes: Long = (TimeUnit.MILLISECONDS.toMinutes(this) % 60)
    var formattedRemainingTime = ""

    if (hours != 0L) {
        formattedRemainingTime += "$hours h "
    }
    if (minutes != 0L) {
        formattedRemainingTime += "$minutes m "
    }
    if (seconds != 0L) {
        formattedRemainingTime += "$seconds s "
    }
    return formattedRemainingTime.trim()
}
