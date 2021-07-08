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

package com.elbehiry.dindinn.ingredients.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.elbehiry.dindinn.databinding.IngredientsPageFragmentView
import com.elbehiry.dindinn.ingredients.viewmodel.IngredientsActions
import com.elbehiry.dindinn.ingredients.viewmodel.IngredientsViewModel
import com.elbehiry.shared.domain.ingredients.IngredientsListPartialState
import com.elbehiry.shared.domain.ingredients.IngredientsViewState
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber

@AndroidEntryPoint
class IngredientsPageFragment : Fragment() {

    lateinit var binding: IngredientsPageFragmentView
    private val ingredientsViewModel: IngredientsViewModel by viewModels()
    private val pageListAdapter by lazy {
        IngredientsPageListAdapter()
    }

    companion object {
        private const val categoryId = "categoryId"

        @JvmStatic
        fun newInstance(id: Int?): IngredientsPageFragment {
            return IngredientsPageFragment().apply {
                arguments = Bundle().apply {
                    putInt(categoryId, id ?: 0)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        with(IngredientsPageFragmentView.inflate(layoutInflater, container, false)) {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.ingredientsPageList.apply {
            layoutManager = GridLayoutManager(
                requireContext(), 2,
                GridLayoutManager.VERTICAL, false
            )
            adapter = pageListAdapter
        }

        ingredientsViewModel.partialStatPublisher
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::render) {
            }

        ingredientsViewModel.states()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::render) {
                Timber.e(it)
            }

        val intent: Observable<IngredientsActions> =
            Observable.just(
                IngredientsActions.GetIngredients(arguments?.getInt(categoryId) ?: 0)
            )
        intent.apply(ingredientsViewModel::processIntents)
    }

    private fun render(partialState: IngredientsListPartialState) {
    }

    private fun render(vs: IngredientsViewState) {
        binding.viewState = vs
        if (!vs.ingredients.isNullOrEmpty()) {
            pageListAdapter.setData(vs.ingredients)
        }
    }
}
