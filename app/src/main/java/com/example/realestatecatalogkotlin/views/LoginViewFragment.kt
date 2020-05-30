package com.example.realestatecatalogkotlin.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SkipStrategy::class)
interface LoginViewFragment : MvpView {
    fun loginOk()
    fun loginError(message: Int)
    fun loginError(message: String)
}