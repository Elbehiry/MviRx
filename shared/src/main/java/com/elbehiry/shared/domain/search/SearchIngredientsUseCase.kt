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

package com.elbehiry.shared.domain.search

import com.elbehiry.shared.base.UseCase
import com.elbehiry.shared.data.ingredient.repository.IGetIngredientsRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SearchIngredientsUseCase @Inject constructor(
    private val ingredientsRepository: IGetIngredientsRepository
) : UseCase<SearchIngredientsUseCase.Params, Observable<SearchPartialState>>() {

    override fun execute(parameters: Params): Observable<SearchPartialState> {
        return ingredientsRepository.searchIngredients(parameters.searchQuery).toObservable()
            .subscribeOn(Schedulers.io())
            .startWithItem(SearchPartialState.Loading)
    }

    class Params private constructor(
        val searchQuery: String
    ) {
        companion object {
            @JvmStatic
            fun create(searchQuery: String): Params {
                return Params(searchQuery)
            }
        }
    }
}
