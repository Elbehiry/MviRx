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

import android.os.CountDownTimer
import com.elbehiry.dindinn.utils.formatPassedTimeToString
import com.elbehiry.dindinn.utils.millisToSeconds

class CountDownTask(
    private val futureInMillis: Long,
    private val alertAtTimeInMillis: Long,
    val onAlertTriggered: (Unit) -> Unit,
    val onFinished: (Unit) -> Unit,
    val onTickTriggered: (String) -> Unit
) : CountDownTimer(
    futureInMillis, 1000
) {

    override fun onTick(millisUntilFinished: Long) {

        onTickTriggered(millisUntilFinished.formatPassedTimeToString())
        if (
            futureInMillis.millisToSeconds() -
            alertAtTimeInMillis.millisToSeconds() == millisUntilFinished.millisToSeconds()
        ) {
            onAlertTriggered(Unit)
        }
    }

    override fun onFinish() {
        onFinished(Unit)
    }
}
