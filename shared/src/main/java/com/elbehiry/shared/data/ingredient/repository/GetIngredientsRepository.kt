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

package com.elbehiry.shared.data.ingredient.repository

import com.elbehiry.shared.data.ingredient.remote.IGetIngredientsDataSource
import com.elbehiry.shared.domain.ingredients.IngredientsListPartialState
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetIngredientsRepository @Inject constructor(
    private val ingredientsDataSource: IGetIngredientsDataSource
) : IGetIngredientsRepository {

    override fun getIngredients(id: Int): Single<IngredientsListPartialState> {
        return ingredientsDataSource.getIngredients(id)
            .map {
                if (it.isNullOrEmpty()) {
                    IngredientsListPartialState.Empty
                } else {
                    IngredientsListPartialState.Ingredients(it)
                }
            }.onErrorReturn {
                IngredientsListPartialState.Failure(it)
            }
    }

    override fun getIngredientsCategory(): Single<IngredientsListPartialState> {
        return ingredientsDataSource.getIngredientsCategory()
            .map {
                if (it.isNullOrEmpty()) {
                    IngredientsListPartialState.Empty
                } else {
                    IngredientsListPartialState.IngredientsCategory(it)
                }
            }.onErrorReturn {
                IngredientsListPartialState.Failure(it)
            }
    }
}
