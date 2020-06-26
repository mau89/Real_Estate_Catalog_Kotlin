package com.example.realestatecatalogkotlin.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.moduledb.database.EstateDb

@StateStrategyType(AddToEndSingleStrategy::class)

interface ViewsFragment : MvpView {
    fun openEstate(bundle: Long)
    fun viewItemEstate(estateDb: EstateDb?)
}