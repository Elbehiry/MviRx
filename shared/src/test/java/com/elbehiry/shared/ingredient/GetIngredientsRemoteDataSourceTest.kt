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

import com.elbehiry.shared.data.ingredient.remote.GetIngredientsRemoteDataSource
import com.elbehiry.shared.data.ingredient.remote.IGetIngredientsDataSource
import com.elbehiry.shared.data.remote.DinDinnApi
import com.elbehiry.test_shared.INGREDIENTS_CATEGORY_ITEMS
import com.elbehiry.test_shared.INGREDIENTS_ITEMS
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test

class GetIngredientsRemoteDataSourceTest {

    @MockK
    private lateinit var api: DinDinnApi

    private lateinit var getIngredientsRemoteDataSource: IGetIngredientsDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getIngredientsRemoteDataSource = GetIngredientsRemoteDataSource(api)
    }

    @Test
    fun `get ingredients from data source call api get ingredient`() {
        every { api.getIngredientsById(any()) } returns Single.just(INGREDIENTS_ITEMS)
        val testObserver = getIngredientsRemoteDataSource.getIngredients(id = 1).test()
        testObserver.assertResult(INGREDIENTS_ITEMS)
        verify { api.getIngredientsById(any()) }
        testObserver.assertNoErrors().assertComplete().dispose()
    }

    @Test
    fun `given network error occurred, should return Single with error`() {
        val errorMessage = "Network Exception"
        every { api.getIngredientsById(any()) } returns Single.error(Exception(errorMessage))
        val testObserver = getIngredientsRemoteDataSource.getIngredients(id = 1).test()
        testObserver.assertError {
            it.message == errorMessage
        }
        verify { api.getIngredientsById(any()) }
        testObserver.dispose()
    }

    @Test
    fun `get ingredients category from data source call api get ingredient category`() {
        every { api.getIngredientsCategory() } returns Single.just(INGREDIENTS_CATEGORY_ITEMS)
        val testObserver = getIngredientsRemoteDataSource.getIngredientsCategory().test()
        testObserver.assertResult(INGREDIENTS_CATEGORY_ITEMS)
        verify { api.getIngredientsCategory() }
        testObserver.assertNoErrors().assertComplete().dispose()
    }

    @Test
    fun `given network error occurred in category, should return Single with error`() {
        val errorMessage = "Network Exception"
        every { api.getIngredientsCategory() } returns Single.error(Exception(errorMessage))
        val testObserver = getIngredientsRemoteDataSource.getIngredientsCategory().test()
        testObserver.assertError {
            it.message == errorMessage
        }
        verify { api.getIngredientsCategory() }
        testObserver.dispose()
    }

    @Test
    fun `get ingredients search ingredient from data source call api get ingredients`() {
        every { api.searchIngredients(any()) } returns Single.just(INGREDIENTS_ITEMS)
        val testObserver = getIngredientsRemoteDataSource.searchIngredients("query").test()
        testObserver.assertResult(INGREDIENTS_ITEMS)
        verify { api.searchIngredients(any()) }
        testObserver.assertNoErrors().assertComplete().dispose()
    }

    @Test
    fun `given network error occurred in search ingredient, should return Single with error`() {
        val errorMessage = "Network Exception"
        every { api.searchIngredients(any()) } returns Single.error(Exception(errorMessage))
        val testObserver = getIngredientsRemoteDataSource.searchIngredients("query").test()
        testObserver.assertError {
            it.message == errorMessage
        }
        verify { api.searchIngredients(any()) }
        testObserver.dispose()
    }
}
