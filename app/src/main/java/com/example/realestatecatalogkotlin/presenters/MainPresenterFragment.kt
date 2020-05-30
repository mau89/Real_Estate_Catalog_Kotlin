package com.example.realestatecatalogkotlin.presenters

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.realestatecatalogkotlin.di.App
import com.example.realestatecatalogkotlin.views.MainViewFragment

@InjectViewState
class MainPresenterFragment : MvpPresenter<MainViewFragment>() {
    fun openObject(id: Long) {
        val bundle = Bundle()
        bundle.putLong("bundleId", id)
        viewState.openEstate(bundle)
    }

    suspend fun deleteRow(id: Long) {
        App.estateDb.deleteEstate(id)
    }
}