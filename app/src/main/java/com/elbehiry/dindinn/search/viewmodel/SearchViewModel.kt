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

package com.elbehiry.dindinn.search.viewmodel

import com.elbehiry.shared.base.BaseVM
import com.elbehiry.shared.domain.search.SearchIngredientsUseCase
import com.elbehiry.shared.domain.search.SearchPartialState
import com.elbehiry.shared.domain.search.SearchViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchIngredientsUseCase: SearchIngredientsUseCase
) : BaseVM<SearchActions, SearchViewState, SearchPartialState>() {
    override val initialState: SearchViewState by lazy {
        SearchViewState()
    }

    override fun reduce(
        result: SearchPartialState,
        previousState: SearchViewState
    ): SearchViewState {
        return result.reduce(previousState, initialState)
    }

    private val search by lazy {
        ObservableTransformer<SearchActions.Search,
            SearchPartialState> { actions ->
            actions.flatMap { item ->
                Observable.just(item.searchQuery)
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .distinctUntilChanged()
                    .filter { text -> text.isNotEmpty() }
                    .switchMap {
                        searchIngredientsUseCase(SearchIngredientsUseCase.Params.create(it))
                    }
            }
        }
    }

    override fun handle(
        action: Observable<SearchActions>
    ): List<Observable<out SearchPartialState>> =
        listOf(
            action.ofType(SearchActions.Search::class.java)
                .compose(search)
        )
}
