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

package com.elbehiry.shared.ingredient

import com.elbehiry.shared.data.ingredient.remote.IGetIngredientsDataSource
import com.elbehiry.shared.data.ingredient.repository.GetIngredientsRepository
import com.elbehiry.shared.data.ingredient.repository.IGetIngredientsRepository
import com.elbehiry.shared.domain.ingredients.IngredientsListPartialState
import com.elbehiry.shared.domain.search.SearchPartialState
import com.elbehiry.test_shared.INGREDIENTS_CATEGORY_ITEMS
import com.elbehiry.test_shared.INGREDIENTS_ITEMS
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetIngredientsRepositoryTest {

    @MockK
    private lateinit var ingredientsDataSource: IGetIngredientsDataSource
    private lateinit var getIngredientsRepository: IGetIngredientsRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getIngredientsRepository = GetIngredientsRepository(ingredientsDataSource)
    }

    @Test
    fun `get ingredients from repository call api remote data source get ingredients`() {
        every { ingredientsDataSource.getIngredients(any()) } returns Single.just(INGREDIENTS_ITEMS)
        val testObserver = getIngredientsRepository.getIngredients(id = 1).test()
        verify { ingredientsDataSource.getIngredients(any()) }
        testObserver.assertNoErrors().assertComplete().dispose()
    }

    @Test
    fun `get ingredients from repository returns ingredients view state`() {
        every { ingredientsDataSource.getIngredients(any()) } returns Single.just(INGREDIENTS_ITEMS)
        val partialState = getIngredientsRepository.getIngredients(id = 1).blockingGet()
        Assert.assertTrue(partialState is IngredientsListPartialState.Ingredients)
        verify { ingredientsDataSource.getIngredients(any()) }
    }

    @Test
    fun `get empty ingredients returns empty view state`() {
        every { ingredientsDataSource.getIngredients(any()) } returns Single.just(emptyList())
        val partialState = getIngredientsRepository.getIngredients(id = 1).blockingGet()
        Assert.assertTrue(partialState is IngredientsListPartialState.Empty)
        verify { ingredientsDataSource.getIngredients(any()) }
    }

    @Test
    fun `get ingredients with error from repository returns error view state`() {
        val errorMessage = "Network Exception"
        every { ingredientsDataSource.getIngredients(any()) } returns Single.error(
            Exception(
                errorMessage
            )
        )
        val partialState = getIngredientsRepository.getIngredients(id = 1).blockingGet()
        Assert.assertTrue(partialState is IngredientsListPartialState.Failure)
        verify { ingredientsDataSource.getIngredients(any()) }
    }

    @Test
    fun `search ingredients from repository call api remote data source search ingredients`() {
        every { ingredientsDataSource.searchIngredients(any()) } returns Single.just(
            INGREDIENTS_ITEMS
        )
        val testObserver = getIngredientsRepository.searchIngredients("query").test()
        verify { ingredientsDataSource.searchIngredients(any()) }
        testObserver.assertNoErrors().assertComplete().dispose()
    }

    @Test
    fun `search ingredients category from repository returns ingredients search view state`() {
        every { ingredientsDataSource.searchIngredients(any()) } returns Single.just(
            INGREDIENTS_ITEMS
        )
        val partialState = getIngredientsRepository.searchIngredients("query").blockingGet()
        Assert.assertTrue(partialState is SearchPartialState.Ingredients)
        verify { ingredientsDataSource.searchIngredients(any()) }
    }

    @Test
    fun `search empty ingredients category returns empty view state`() {
        every { ingredientsDataSource.searchIngredients(any()) } returns Single.just(emptyList())
        val partialState = getIngredientsRepository.searchIngredients("query").blockingGet()
        Assert.assertTrue(partialState is SearchPartialState.Empty)
        verify { ingredientsDataSource.searchIngredients(any()) }
    }

    @Test
    fun `search ingredients category with error from repository returns error view state`() {
        val errorMessage = "Network Exception"
        every { ingredientsDataSource.searchIngredients(any()) } returns Single.error(
            Exception(
                errorMessage
            )
        )
        val partialState = getIngredientsRepository.searchIngredients("query").blockingGet()
        Assert.assertTrue(partialState is SearchPartialState.Failure)
        verify { ingredientsDataSource.searchIngredients(any()) }
    }

    @Test
    fun `get ingredients category from repository call api get ingredients category`() {
        every { ingredientsDataSource.getIngredientsCategory() } returns Single.just(
            INGREDIENTS_CATEGORY_ITEMS
        )
        val testObserver = getIngredientsRepository.getIngredientsCategory().test()
        verify { ingredientsDataSource.getIngredientsCategory() }
        testObserver.assertNoErrors().assertComplete().dispose()
    }

    @Test
    fun `get ingredients category from repository returns ingredients category view state`() {
        every { ingredientsDataSource.getIngredientsCategory() } returns Single.just(
            INGREDIENTS_CATEGORY_ITEMS
        )
        val partialState = getIngredientsRepository.getIngredientsCategory().blockingGet()
        Assert.assertTrue(partialState is IngredientsListPartialState.IngredientsCategory)
        verify { ingredientsDataSource.getIngredientsCategory() }
    }

    @Test
    fun `get empty ingredients category returns empty view state`() {
        every { ingredientsDataSource.getIngredientsCategory() } returns Single.just(emptyList())
        val partialState = getIngredientsRepository.getIngredientsCategory().blockingGet()
        Assert.assertTrue(partialState is IngredientsListPartialState.Empty)
        verify { ingredientsDataSource.getIngredientsCategory() }
    }

    @Test
    fun `get ingredients category with error from repository returns error view state`() {
        val errorMessage = "Network Exception"
        every { ingredientsDataSource.getIngredientsCategory() } returns Single.error(
            Exception(
                errorMessage
            )
        )
        val partialState = getIngredientsRepository.getIngredientsCategory().blockingGet()
        Assert.assertTrue(partialState is IngredientsListPartialState.Failure)
        verify { ingredientsDataSource.getIngredientsCategory() }
    }
}
