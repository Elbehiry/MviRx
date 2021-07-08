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

package com.elbehiry.shared.domain.ingredients

import com.elbehiry.model.IngredientsCategoryItem
import com.elbehiry.model.IngredientsItem
import com.elbehiry.shared.base.MVIPartialState

sealed class IngredientsListPartialState : MVIPartialState<IngredientsViewState> {
    abstract override fun reduce(
        oldVs: IngredientsViewState,
        initialVs: IngredientsViewState
    ): IngredientsViewState

    object Empty : IngredientsListPartialState() {
        override fun reduce(
            oldVs: IngredientsViewState,
            initialVs: IngredientsViewState
        ): IngredientsViewState {
            return initialVs.copy(empty = true)
        }
    }

    class Failure(private val throwable: Throwable) : IngredientsListPartialState() {
        override fun reduce(
            oldVs: IngredientsViewState,
            initialVs: IngredientsViewState
        ): IngredientsViewState {
            return initialVs.copy(error = throwable)
        }
    }

    object Loading : IngredientsListPartialState() {
        override fun reduce(
            oldVs: IngredientsViewState,
            initialVs: IngredientsViewState
        ): IngredientsViewState {
            return initialVs.copy(loading = true)
        }
    }

    class Ingredients(private val ingredients: List<IngredientsItem>) :
        IngredientsListPartialState() {
        override fun reduce(
            oldVs: IngredientsViewState,
            initialVs: IngredientsViewState
        ): IngredientsViewState {
            return initialVs.copy(
                ingredients = ingredients
            )
        }
    }

    class IngredientsCategory(private val ingredients: List<IngredientsCategoryItem>) :
        IngredientsListPartialState() {
        override fun reduce(
            oldVs: IngredientsViewState,
            initialVs: IngredientsViewState
        ): IngredientsViewState {
            return initialVs.copy(ingredientsCategory = ingredients)
        }
    }
}
