package com.example.realestatecatalogkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.moduledb.database.EstateDb
import com.example.realestatecatalogkotlin.Helperss.Helpers
import com.example.realestatecatalogkotlin.R
import com.example.realestatecatalogkotlin.presenters.EditPresenter
import com.example.realestatecatalogkotlin.views.EditViewFragment
import kotlinx.android.synthetic.main.edit_fragment.*
import kotlin.properties.Delegates

class EditFragment : BaseFragment(), EditViewFragment {

    private var estateId by Delegates.notNull<Long>()
    private lateinit var photo: String
    private lateinit var address: String

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
        editPresenter.getEstate(key)
        btn_edit_property.setOnClickListener {
            editPresenter.saveEstate(
                estateId,
                photo,
                address,
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

    override fun editItemEstate(estateDb: EstateDb?) {
        if (estateDb != null) {
            estateId = estateDb.estateId
            photo = estateDb.photo
            address = estateDb.address
            text_edit_area_property.setText(estateDb.area.toBigDecimal().toPlainString())
            text_edit_price_property.setText(estateDb.price.toBigDecimal().toPlainString())
            text_edit_quantity_room_property.setText(estateDb.quantity_room.toString())
            text_edit_floor_property.setText(estateDb.floor.toString())
        }
    }
}
