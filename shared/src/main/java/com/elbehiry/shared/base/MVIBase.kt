package com.elbehiry.shared.base

interface MVIAction
interface MVIViewState
interface MVIPartialState<S:MVIViewState>{
    fun reduce(oldVs:S,initialVs:S):S
}