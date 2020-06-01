package com.example.realestatecatalogkotlin.presenters

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.realestatecatalogkotlin.database.EstateDb
import com.example.realestatecatalogkotlin.di.App
import com.example.realestatecatalogkotlin.views.EditViewFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal

@InjectViewState
class EditPresenter : MvpPresenter<EditViewFragment>() {
    var errorPhoto = false
    var errorAddress = false
    var errorArea = false
    var errorPrice = false
    var errorQuantityRoom = false
    var errorFloor = false

    fun saveEstate(
        idEstate: Long,
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
            val estateDb = App.estateDb
            val mEstateDb = EstateDb(
                photo,
                address,
                area.toDouble(),
                price.toDouble(),
                quantity_room.toInt(),
                floor.toInt(),
                priceSqm
            )
            GlobalScope.launch(Dispatchers.Main) {
                mEstateDb.estateId = idEstate
                estateDb.updateEstate(
                    mEstateDb
                )
            }
            val bundle = Bundle()
            bundle.putLong("bundleId", idEstate)
            viewState.saveEdit(bundle)
        } else {
            viewState.showError(
                errorPhoto,
                errorAddress,
                errorArea,
                errorPrice
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