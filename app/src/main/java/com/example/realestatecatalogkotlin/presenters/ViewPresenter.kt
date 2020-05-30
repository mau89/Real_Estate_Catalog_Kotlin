package com.example.realestatecatalogkotlin.presenters

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.realestatecatalogkotlin.views.ViewsFragment

@InjectViewState
class ViewPresenter : MvpPresenter<ViewsFragment>() {
    fun addBundle(id: Long) {
        val bundle = Bundle()
        bundle.putLong("bundleId", id)
        viewState.openEstate(id)
    }
}