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

package com.elbehiry.dindinn.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.elbehiry.dindinn.databinding.IngredientSearchLayout
import com.elbehiry.dindinn.ingredients.page.IngredientsPageListAdapter
import com.elbehiry.dindinn.search.viewmodel.SearchActions
import com.elbehiry.dindinn.search.viewmodel.SearchViewModel
import com.elbehiry.shared.domain.search.SearchPartialState
import com.elbehiry.shared.domain.search.SearchViewState
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: IngredientSearchLayout
    private val searchViewModel: SearchViewModel by viewModels()
    private val searchPublisher by lazy { PublishSubject.create<String>() }
    private val pageListAdapter by lazy {
        IngredientsPageListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        with(IngredientSearchLayout.inflate(layoutInflater, container, false)) {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureSearchView()
        binding.ingredientsSearchList.apply {
            layoutManager = GridLayoutManager(
                requireContext(), 2,
                GridLayoutManager.VERTICAL, false
            )
            adapter = pageListAdapter
        }

        searchViewModel.partialStatPublisher
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::render) {
            }

        searchViewModel.states()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::render) {
                Timber.e(it)
            }

        intents().apply(searchViewModel::processIntents)
    }

    private fun configureSearchView() {
        binding.searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    dismissKeyboard(this@apply)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    searchPublisher.onNext(newText)
                    return true
                }
            })

            setOnQueryTextFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    showKeyboard(view.findFocus())
                }
            }
            requestFocus()
        }
    }

    private fun render(partialState: SearchPartialState) {
    }

    private fun render(vs: SearchViewState) {
        binding.viewState = vs
        if (!vs.ingredients.isNullOrEmpty()) {
            pageListAdapter.setData(vs.ingredients)
        }
    }

    private fun showKeyboard(view: View) {
        ViewCompat.getWindowInsetsController(view)?.show(WindowInsetsCompat.Type.ime())
    }

    private fun dismissKeyboard(view: View) {
        ViewCompat.getWindowInsetsController(view)?.hide(WindowInsetsCompat.Type.ime())
    }

    private fun intents(): Observable<SearchActions> = searchPublisher.map {
        SearchActions.Search(it)
    }
}
