package com.example.realestatecatalogkotlin.views

import android.os.Bundle
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SkipStrategy::class)

interface MainViewFragment : MvpView {
    fun openEstate(bundle: Bundle)
}