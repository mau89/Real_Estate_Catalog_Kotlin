package com.example.realestatecatalogkotlin.views

import android.os.Bundle
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SkipStrategy::class)
interface AddViewFragment : MvpView {
    fun saveEstate(massage: Int, bundle: Bundle)
    fun showError(
        errorPhoto: Boolean,
        errorAddress: Boolean,
        errorArea: Boolean,
        errorPrice: Boolean,
        errorQuantityRoom: Boolean,
        errorFloor: Boolean
    )
}