package com.example.realestatecatalogkotlin.fragments

import android.os.Bundle
import android.view.*
import androidx.navigation.Navigation
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.realestatecatalogkotlin.Helperss.Helpers
import com.example.realestatecatalogkotlin.R
import com.example.realestatecatalogkotlin.di.App
import com.example.realestatecatalogkotlin.presenters.ViewPresenter
import com.example.realestatecatalogkotlin.views.ViewsFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_fragment.*
import kotlinx.coroutines.launch
import java.io.File

class ViewFragment : BaseFragment(), ViewsFragment {
    private val estateDb = App.estateDb
    var bundleId = -1L

    @InjectPresenter
    lateinit var viewPresenter: ViewPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        Helpers.stopTimer()
        Helpers.startTimer()
        val key = requireArguments().getLong("bundleId")
        viewPresenter.addBundle(key)
        if (key != -1L) {
            launch {
                val viewEstateDb = estateDb.getEstate(key)
                Picasso.get().load(File(viewEstateDb.photo)).into(image_view_property)
                text_view_price_property.text = viewEstateDb.price.toBigDecimal().toPlainString()
                text_view_area_property.text = viewEstateDb.area.toBigDecimal().toPlainString()
                text_view_address_property.text = viewEstateDb.address
                text_view_quantity_room_property.text = viewEstateDb.quantity_room.toString()
                text_view_price_sqm_property.text =
                    viewEstateDb.price_sqm.toBigDecimal().toPlainString()
                text_view_floor_property.text = viewEstateDb.floor.toString()
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_edit_property).isVisible = true
        menu.findItem(R.id.action_add_property).isVisible = false
        menu.findItem(R.id.action_exit).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun openEstate(bundle: Long) {
        bundleId = bundle
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit_property -> {
                val bundle = Bundle()
                bundle.putLong("bundleId", bundleId)
                view?.let {
                    Navigation.findNavController(it)
                        .navigate(R.id.action_viewFragment_to_editFragment, bundle)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
