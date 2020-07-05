package com.example.realestatecatalogkotlin.views

import android.os.Bundle
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.moduledb.database.EstateDb

@StateStrategyType(SkipStrategy::class)
interface EditViewFragment : MvpView {
    fun saveEdit(massage: Bundle)
    fun showError(
        errorArea: Boolean,
        errorPrice: Boolean,
        errorQuantityRoom: Boolean,
        errorFloor: Boolean
    )
    fun editItemEstate(estateDb: EstateDb?)
}