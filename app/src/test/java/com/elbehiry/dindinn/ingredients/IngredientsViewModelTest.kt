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

package com.elbehiry.dindinn.ingredients

import com.elbehiry.dindinn.ingredients.viewmodel.IngredientsActions
import com.elbehiry.dindinn.ingredients.viewmodel.IngredientsViewModel
import com.elbehiry.shared.domain.ingredients.GetIngredientsCategoryListUseCase
import com.elbehiry.shared.domain.ingredients.GetIngredientsListUseCase
import com.elbehiry.shared.domain.ingredients.IngredientsListPartialState
import com.elbehiry.shared.domain.ingredients.IngredientsViewState
import com.elbehiry.test_shared.INGREDIENTS_CATEGORY_ITEMS
import com.elbehiry.test_shared.INGREDIENTS_ITEMS
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Test

class IngredientsViewModelTest {

    @MockK
    private lateinit var getIngredientsCategoryListUseCase: GetIngredientsCategoryListUseCase

    @MockK
    private lateinit var getIngredientsListUseCase: GetIngredientsListUseCase

    private lateinit var ingredientsViewModel: IngredientsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getIngredientsCategoryListUseCase = mockk()
        getIngredientsListUseCase = mockk()
        ingredientsViewModel = IngredientsViewModel(
            getIngredientsCategoryListUseCase,
            getIngredientsListUseCase
        )
    }

    @Test
    fun `should emit initial state without initial query when initial query empty`() {
        ingredientsViewModel.states().test().assertValue(IngredientsViewState())
    }

    @Test
    fun `get ingredients should start with loading`() {
        every { getIngredientsListUseCase(any()) } returns Observable.just(
            IngredientsListPartialState.Loading
        )
        val testObserver = ingredientsViewModel.states().test()
        ingredientsViewModel.processIntents(
            Observable.just(IngredientsActions.GetIngredients(1))
        )
        testObserver.assertValueAt(1) { it.loading }
    }

    @Test
    fun `get ingredients category should start with loading`() {
        every { getIngredientsCategoryListUseCase(Unit) } returns Observable.just(
            IngredientsListPartialState.Loading
        )
        val testObserver = ingredientsViewModel.states().test()
        ingredientsViewModel.processIntents(
            Observable.just(IngredientsActions.GetIngredientsCategory)
        )
        testObserver.assertValueAt(1) { it.loading }
    }

    @Test
    fun `get ingredients list should return the right state`() {
        every { getIngredientsListUseCase(any()) } returns Observable.just(
            IngredientsListPartialState.Ingredients(INGREDIENTS_ITEMS)
        )
        val testObserver = ingredientsViewModel.states().test()
        ingredientsViewModel.processIntents(
            Observable.just(IngredientsActions.GetIngredients(1))
        )
        testObserver.assertValueAt(1) { it.ingredients == INGREDIENTS_ITEMS }
    }

    @Test
    fun `get ingredients category list should return the right state`() {
        every { getIngredientsCategoryListUseCase(Unit) } returns Observable.just(
            IngredientsListPartialState.IngredientsCategory(INGREDIENTS_CATEGORY_ITEMS)
        )
        val testObserver = ingredientsViewModel.states().test()
        ingredientsViewModel.processIntents(
            Observable.just(IngredientsActions.GetIngredientsCategory)
        )
        testObserver.assertValueAt(1) { it.ingredientsCategory == INGREDIENTS_CATEGORY_ITEMS }
    }
}
