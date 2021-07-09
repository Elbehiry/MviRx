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

plugins {
    id("java-platform")
}

val core = "1.5.0-beta01"
val appCompat = "1.2.0-rc01"
val material = "1.3.0"
val hilt = Versions.HILT
val hiltJetPack = "1.0.0-alpha01"
val glide = "4.12.0"
val gson = "2.8.6"
val lifecycle = "2.3.1"
val lifecycleExt = "2.2.0"
val fragmentKtx = "1.3.2"
val okhttp = "4.8.1"
val retrofit = "2.9.0"
val constraint = "2.0.4"
val recyclerview = "1.2.0-alpha05"
val rxAndroid = "3.0.0"
val rxJava = "3.0.11"
val rxKotlin = "3.0.1"
val junit = "4.12"
val extJunit = "1.1.2"
val espresso = "3.3.0"
val retrofitConverter = "3.0.0"
val timber = "4.7.1"
val navigation = "2.3.4"
val swipeRefreshLayout = "1.2.0-alpha01"
val rxSwipeRefreshLayout = "4.0.0"
val assertJVersion = "3.19.0"
val mockkVersion = "1.10.6"
val faker = "1.0.2"

dependencies {
    constraints {
        api("${Libs.KOTLIN_STDLIB}:${Versions.KOTLIN}")
        api("${Libs.CORE_KTX}:$core")
        api("${Libs.APP_COMPAT}:$appCompat")
        api("${Libs.ANDROIDX_HILT_COMPILER}:$hiltJetPack")
        api("${Libs.HILT_ANDROID}:$hilt")
        api("${Libs.HILT_COMPILER}:$hilt")
        api("${Libs.HILT_TESTING}:$hilt")
        api("${Libs.HILT_VIEWMODEL}:$hiltJetPack")
        api("${Libs.ANDROIDX_HILT_COMPILER}:$hiltJetPack")
        api("${Libs.CORE_KTX}:$core")
        api("${Libs.APP_COMPAT}:$appCompat")
        api("${Libs.GLIDE}:$glide")
        api("${Libs.GLIDE_COMPILER}:$glide")
        api("${Libs.GSON}:$gson")
        api("${Libs.LIFECYCLE_VIEW_MODEL_KTX}:$lifecycle")
        api("${Libs.FRAGMENT_KTX}:$fragmentKtx")
        api("${Libs.OKHTTP}:$okhttp")
        api("${Libs.OKHTTP_LOGGING_INTERCEPTOR}:$okhttp")
        api("${Libs.OKHTTP_MOCK_SERVER}:$okhttp")
        api("${Libs.RETROFIT}:$retrofit")
        api("${Libs.GSON_CONVERTER}:$retrofit")
        api("${Libs.RETROFIT_CONVERTER}:$retrofitConverter")
        api("${Libs.LIFECYCLE_RUN_TIME}:$lifecycle")
        api("${Libs.CONSTRAINT_LAYOUT}:$constraint")
        api("${Libs.MATERIAL}:$material")
        api("${Libs.RECYCLERVIEW}:$recyclerview")
        api("${Libs.RX_ANDROID}:$rxAndroid")
        api("${Libs.RX_JAVA}:$rxJava")
        api("${Libs.RX_KOTLIN}:$rxKotlin")
        api("${Libs.JUNIT}:$junit")
        api("${Libs.JUNIT_EXT}:$extJunit")
        api("${Libs.ESPRESSO}:$espresso")
        api("${Libs.TIMBER}:$timber")
        api("${Libs.NAVIGATION_KTX}:$navigation")
        api("${Libs.NAVIGATION_UI}:$navigation")
        api("${Libs.SWIPE_REFRESH_LAYOUT}:$swipeRefreshLayout")
        api("${Libs.RX_SWIPE_REFRESH_LAYOUT}:$rxSwipeRefreshLayout")
        api("${Libs.FAKER}:$faker")
        api("${Libs.ASSERT_J}:$assertJVersion")
        api("${Libs.MOCKK}:$mockkVersion")
    }
}
