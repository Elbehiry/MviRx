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

package com.elbehiry.test_shared

import com.elbehiry.model.IngredientsCategoryItem
import com.elbehiry.model.IngredientsItem
import com.elbehiry.model.OrdersItem
import com.github.javafaker.Faker
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

const val backendGeneralFormat = "yyyy-MM-dd'T'hh:mm:ssZ"

val ORDER_ID = Faker().number().digits(3).toInt()
val order = "{id:$ORDER_ID}"
val orders = "[$order]"

val faker by lazy {
    Faker()
}

val ORDER_ITEM = OrdersItem(
    id = faker.number().digits(3).toInt(),
    title = faker.lorem().sentence(),
    quantity = faker.number().digits(2).toInt(),
    createdAt = faker.date().past(2, TimeUnit.HOURS).toBackEndFormat(),
    alertedAt = faker.date().future(2, TimeUnit.MINUTES).toBackEndFormat(),
    expiredAt = faker.date().future(2, TimeUnit.HOURS).toBackEndFormat(),
)

val INGREDIENT_ITEM = IngredientsItem(
    id = faker.number().digits(3).toInt(),
    title = faker.lorem().sentence(),
    available = faker.number().digits(2).toInt(),
    image = faker.internet().image()
)

val INGREDIENT_CATEGORY = IngredientsCategoryItem(
    id = faker.number().digits(3).toInt(),
    title = faker.lorem().sentence()
)

val ORDERS_ITEMS = listOf(
    ORDER_ITEM.copy(id = faker.number().digits(3).toInt()),
    ORDER_ITEM.copy(id = faker.number().digits(3).toInt()),
    ORDER_ITEM.copy(id = faker.number().digits(3).toInt()),
    ORDER_ITEM.copy(id = faker.number().digits(3).toInt())
)

val INGREDIENTS_ITEMS = listOf(
    INGREDIENT_ITEM.copy(id = faker.number().digits(3).toInt()),
    INGREDIENT_ITEM.copy(id = faker.number().digits(3).toInt()),
    INGREDIENT_ITEM.copy(id = faker.number().digits(3).toInt()),
    INGREDIENT_ITEM.copy(id = faker.number().digits(3).toInt())
)

val INGREDIENTS_CATEGORY_ITEMS = listOf(
    INGREDIENT_CATEGORY.copy(id = faker.number().digits(3).toInt()),
    INGREDIENT_CATEGORY.copy(id = faker.number().digits(3).toInt()),
    INGREDIENT_CATEGORY.copy(id = faker.number().digits(3).toInt()),
    INGREDIENT_CATEGORY.copy(id = faker.number().digits(3).toInt())
)

private fun Date.toBackEndFormat(): String {
    val outputFormatter: DateFormat = SimpleDateFormat(backendGeneralFormat, Locale.getDefault())
    return outputFormatter.format(this)
}
