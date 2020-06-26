package com.example.realestatecatalogkotlin.presenters

import android.os.Bundle
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.moduledb.database.EstateDb
import com.example.realestatecatalogkotlin.di.App
import com.example.realestatecatalogkotlin.views.ViewsFragment
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class ViewPresenter : MvpPresenter<ViewsFragment>() {
    private val estateDb = App.estateDb

    fun addBundle(id: Long) {
        val bundle = Bundle()
        bundle.putLong("bundleId", id)
        viewState.openEstate(id)
    }

    fun getEstate(key: Long) {
        Log.d("TAG", "зашел в процедуру")
        if (key != -1L) {
            val observable = getItemEstate(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    returnValue(it)
                }, {})
            Log.d("TAG", "в вышел из него")
        } else {
            returnValue(null)
        }
    }

    private fun returnValue(it: EstateDb?) {
        viewState.viewItemEstate(it)
    }

    private fun getItemEstate(key: Long): Single<EstateDb> {
        return Single.create { subscriber ->
            val getEstateDb = estateDb.getEstate(key)
            subscriber.onSuccess(getEstateDb)
        }
    }
}