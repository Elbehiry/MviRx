package com.elbehiry.dindinn.search

import com.elbehiry.dindinn.search.viewmodel.SearchActions
import com.elbehiry.dindinn.search.viewmodel.SearchViewModel
import com.elbehiry.shared.domain.search.SearchIngredientsUseCase
import com.elbehiry.shared.domain.search.SearchPartialState
import com.elbehiry.shared.domain.search.SearchViewState
import com.elbehiry.test_shared.INGREDIENTS_ITEMS
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Test

class SearchViewModelTest {
    @MockK
    private lateinit var searchIngredientsUseCase: SearchIngredientsUseCase

    private lateinit var searchViewModel: SearchViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        searchIngredientsUseCase = mockk()
        searchViewModel = SearchViewModel(
            searchIngredientsUseCase
        )
    }

    @Test
    fun `should emit initial state without initial query when initial query empty`() {
        searchViewModel.states().test().assertValue(SearchViewState())
    }

    @Test
    fun `search ingredients should start with loading`() {
        every { searchIngredientsUseCase(any()) } returns Observable.just(
            SearchPartialState.Loading
        )
        val testObserver = searchViewModel.states().test()
        searchViewModel.processIntents(
            Observable.just(SearchActions.Search("query"))
        )
        testObserver.assertValueAt(1) { it.loading }
    }

    @Test
    fun `search ingredients list should return the right state`() {
        every { searchIngredientsUseCase(any()) } returns Observable.just(
            SearchPartialState.Ingredients(INGREDIENTS_ITEMS)
        )
        val testObserver = searchViewModel.states().test()
        searchViewModel.processIntents(
            Observable.just(SearchActions.Search("query"))
        )
        testObserver.assertValueAt(1) { it.ingredients == INGREDIENTS_ITEMS }
    }
}