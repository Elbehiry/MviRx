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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elbehiry.dindinn.R
import com.elbehiry.dindinn.databinding.IngredientsFragmentView
import com.elbehiry.dindinn.ingredients.page.IngredientsPageFragment
import com.elbehiry.dindinn.ingredients.viewmodel.IngredientsActions
import com.elbehiry.dindinn.ingredients.viewmodel.IngredientsViewModel
import com.elbehiry.model.IngredientsCategoryItem
import com.elbehiry.shared.domain.ingredients.IngredientsListPartialState
import com.elbehiry.shared.domain.ingredients.IngredientsViewState
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber

@AndroidEntryPoint
class IngredientsFragment : Fragment() {

    private lateinit var binding: IngredientsFragmentView
    private val ingredientsViewModel: IngredientsViewModel by viewModels()
    private lateinit var pagerAdapter: SectionPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        with(IngredientsFragmentView.inflate(layoutInflater, container, false)) {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            pagerAdapter = SectionPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
            adapter = pagerAdapter
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        changeSearchViewStyle()
        ingredientsViewModel.partialStatPublisher
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::render) {
            }

        ingredientsViewModel.states()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::render) { Timber.e(it) }

        intents().apply(ingredientsViewModel::processIntents)
    }

    private fun render(partialState: IngredientsListPartialState) {
    }

    private fun render(vs: IngredientsViewState) {
        binding.viewState = vs
        configureTabs(vs.ingredientsCategory)
    }

    private fun configureTabs(ingredientsCategory: List<IngredientsCategoryItem>) {
        if (ingredientsCategory.isNotEmpty()) {
            for (item in ingredientsCategory) {
                pagerAdapter.addFragment(IngredientsPageFragment.newInstance(item.id))
            }

            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
                tab.text = ingredientsCategory[pos].title
            }.attach()
        }
    }

    private fun changeSearchViewStyle() {
        val text = binding.searchIngredients.findViewById<TextView>(R.id.search_src_text)
        text.setTextColor(ContextCompat.getColor(requireActivity(), R.color.grayCal))
        text.setHintTextColor(ContextCompat.getColor(requireActivity(), R.color.grayCal))

        val imvSearch: ImageView = binding.searchIngredients.findViewById(R.id.search_mag_icon)
        imvSearch.setImageResource(R.drawable.ic_round_search_24)

        val imvClose: ImageView = binding.searchIngredients.findViewById(R.id.search_close_btn)
        imvClose.setImageResource(R.drawable.ic_baseline_close_24)
    }

    private fun intents(): Observable<IngredientsActions> =
        Observable.just(IngredientsActions.GetIngredientsCategory)
}
