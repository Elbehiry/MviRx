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

package com.elbehiry.dindinn.ingredients.viewmodel

import com.elbehiry.shared.base.BaseVM
import com.elbehiry.shared.domain.ingredients.GetIngredientsCategoryListUseCase
import com.elbehiry.shared.domain.ingredients.GetIngredientsListUseCase
import com.elbehiry.shared.domain.ingredients.IngredientsViewState
import com.elbehiry.shared.domain.ingredients.IngredientsListPartialState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

@HiltViewModel
class IngredientsViewModel @Inject constructor(
    private val getIngredientsCategoryListUseCase: GetIngredientsCategoryListUseCase,
    private val getIngredientsListUseCase: GetIngredientsListUseCase
) : BaseVM<IngredientsActions, IngredientsViewState, IngredientsListPartialState>() {

    override val initialState by lazy { IngredientsViewState() }

    override fun reduce(
        result: IngredientsListPartialState,
        previousState: IngredientsViewState
    ): IngredientsViewState {
        return result.reduce(previousState, initialState)
    }

    private val getIngredientsCategory by lazy {
        ObservableTransformer<IngredientsActions.GetIngredientsCategory,
            IngredientsListPartialState> { actions ->
            actions.flatMap {
                getIngredientsCategoryListUseCase(Unit)
            }
        }
    }

    private val getIngredients by lazy {
        ObservableTransformer<IngredientsActions.GetIngredients,
            IngredientsListPartialState> { actions ->
            actions.flatMap {
                val id = GetIngredientsListUseCase.Params.create(it.categoryId)
                getIngredientsListUseCase(id)
            }
        }
    }

    override fun handle(
        action: Observable<IngredientsActions>
    ): List<Observable<out IngredientsListPartialState>> =
        listOf(
            action.ofType(IngredientsActions.GetIngredientsCategory::class.java)
                .take(1)
                .compose(getIngredientsCategory),
            action.ofType(IngredientsActions.GetIngredients::class.java)
                .compose(getIngredients)
        )
}
