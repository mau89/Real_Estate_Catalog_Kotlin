package com.example.realestatecatalogkotlin.presenters

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.realestatecatalogkotlin.R
import com.example.realestatecatalogkotlin.database.EstateDb
import com.example.realestatecatalogkotlin.di.App
import com.example.realestatecatalogkotlin.views.AddViewFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal

@InjectViewState
class AddPresenter : MvpPresenter<AddViewFragment>() {
    var errorPhoto = false
    var errorAddress = false
    var errorArea = false
    var errorPrice = false
    var errorQuantityRoom = false
    var errorFloor = false
    private val db = App.estateDatabase

    fun saveEstatePresenter(
        photo: String,
        address: String,
        area: String,
        price: String,
        quantity_room: String,
        floor: String
    ) {
        if (checkInput(photo, address, area, price, quantity_room, floor)) {
            val priceSqm = BigDecimal(price.toDouble() / area.toDouble()).setScale(
                2,
                BigDecimal.ROUND_CEILING
            ).toDouble()
            val estateDb = db.estateDbDao
            val estateItem = EstateDb(
                photo,
                address,
                area.toDouble(),
                price.toDouble(),
                quantity_room.toInt(),
                floor.toInt(),
                priceSqm
            )
            GlobalScope.launch(Dispatchers.Main) {
                val bundle = Bundle()
                bundle.putLong(
                    "bundleId", estateDb.insertEstate(
                        estateItem
                    )
                )
                viewState.saveEstate(R.string.saveOk, bundle)
            }
        } else {
            viewState.showError(
                errorPhoto,
                errorAddress,
                errorArea,
                errorPrice,
                errorQuantityRoom,
                errorFloor
            )
        }
    }

    private fun checkInput(
        photo: String,
        address: String,
        area: String,
        price: String,
        quantity_room: String,
        floor: String
    ): Boolean {
        errorPhoto = photo.isEmpty()
        errorAddress = address.isEmpty()
        errorArea = area.isEmpty()
        errorPrice = price.isEmpty()
        errorQuantityRoom = quantity_room.isEmpty()
        errorFloor = floor.isEmpty()
        return !(errorPhoto || errorAddress || errorArea || errorPrice || errorQuantityRoom || errorFloor)
    }
}