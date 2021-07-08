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

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elbehiry.dindinn.utils.propagateTo
import com.elbehiry.model.IngredientsItem
import kotlin.properties.Delegates

class IngredientsPageListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var ingredients by Delegates.observable(mutableListOf<IngredientsItem>()) { _, old, new ->
        propagateTo(old) {
            newItems = new
            compareItems = { old, new -> old.id == new.id }
        }
    }

    fun setData(newList: List<IngredientsItem>) {
        ingredients = newList.toMutableList()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return IngredientsPageViewHolder initializeWith parent
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is IngredientsPageViewHolder)
            holder.bind(ingredients[position])
    }

    override fun getItemCount(): Int = ingredients.size
}
