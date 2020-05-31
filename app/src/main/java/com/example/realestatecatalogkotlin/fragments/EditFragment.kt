package com.example.realestatecatalogkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.realestatecatalogkotlin.Helperss.Helpers
import com.example.realestatecatalogkotlin.R
import com.example.realestatecatalogkotlin.database.EstateDb
import com.example.realestatecatalogkotlin.di.App
import com.example.realestatecatalogkotlin.presenters.EditPresenter
import com.example.realestatecatalogkotlin.views.EditViewFragment
import kotlinx.android.synthetic.main.edit_fragment.*
import kotlinx.coroutines.launch

class EditFragment : BaseFragment(), EditViewFragment {

    private val estateDb = App.estateDb
    private lateinit var editEstateDb: EstateDb

    @InjectPresenter
    lateinit var editPresenter: EditPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        Helpers.stopTimer()
        Helpers.startTimer()
        val key = requireArguments().getLong("bundleId")
        if (key != -1L) {
            launch {
                editEstateDb = estateDb.getEstate(key)
                text_edit_area_property.setText(editEstateDb.area.toBigDecimal().toPlainString())
                text_edit_price_property.setText(editEstateDb.price.toBigDecimal().toPlainString())
                text_edit_quantity_room_property.setText(editEstateDb.quantity_room.toString())
                text_edit_floor_property.setText(editEstateDb.floor.toString())
            }
        }
        btn_edit_property.setOnClickListener {
            editPresenter.saveEstate(
                editEstateDb.estateId,
                editEstateDb.photo,
                editEstateDb.address,
                text_edit_area_property.text.toString(),
                text_edit_price_property.text.toString(),
                text_edit_quantity_room_property.text.toString(),
                text_edit_floor_property.text.toString()
            )
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_edit_property).isVisible = false
        menu.findItem(R.id.action_add_property).isVisible = false
        menu.findItem(R.id.action_exit).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun saveEdit(massage: Bundle) {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_editFragment_to_viewFragment, massage)
        }
    }

    override fun showError(
        errorArea: Boolean,
        errorPrice: Boolean,
        errorQuantityRoom: Boolean,
        errorFloor: Boolean
    ) {
        if (errorArea) {
            text_edit_area_property_layout.isErrorEnabled = true
            text_edit_area_property_layout.error = "Не указана площадь"
        } else {
            text_edit_area_property_layout.isErrorEnabled = false
        }
        if (errorPrice) {
            text_edit_price_property_layout.isErrorEnabled = true
            text_edit_price_property_layout.error = "Не указана цена"
        } else {
            text_edit_price_property_layout.isErrorEnabled = false
        }
        if (errorQuantityRoom) {
            text_edit_quantity_room_property_layout.isErrorEnabled = true
            text_edit_quantity_room_property_layout.error = "Не указано количество комнат"
        } else {
            text_edit_quantity_room_property_layout.isErrorEnabled = false
        }
        if (errorFloor) {
            text_edit_floor_property_layout.isErrorEnabled = true
            text_edit_floor_property_layout.error = "Не указан этаж"
        } else {
            text_edit_floor_property_layout.isErrorEnabled = false
        }
    }
}
