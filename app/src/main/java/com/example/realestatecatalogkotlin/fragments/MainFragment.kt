package com.example.realestatecatalogkotlin.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.realestatecatalogkotlin.Helperss.Helpers
import com.example.realestatecatalogkotlin.R
import com.example.realestatecatalogkotlin.adapter.EstateAdapter
import com.example.realestatecatalogkotlin.adapter.EstateAdapterCallback
import com.example.realestatecatalogkotlin.di.App
import com.example.realestatecatalogkotlin.presenters.MainPresenterFragment
import com.example.realestatecatalogkotlin.views.MainViewFragment
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.coroutines.launch

class MainFragment : BaseFragment(), MainViewFragment {

    private val mAdapter = EstateAdapter()
    private val estateDb = App.estateDb

    @InjectPresenter
    lateinit var mainPresenter: MainPresenterFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        Helpers.stopTimer()
        Helpers.startTimer()
        setupAdapter()
    }

    private fun setupAdapter() {
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler_property.layoutManager = layoutManager
        recycler_property.adapter = mAdapter
        launch {
            mAdapter.setData(estateDb.getAllItems())
        }
        mAdapter.attachDelegate(object : EstateAdapterCallback {
            override fun openItem(id: Long) {
                mainPresenter.openObject(id)
            }

            override fun deleteRow(id: Long, position: Int, address: String) {
                mAdapter.deleteRow(position)
                launch {
                    mainPresenter.deleteRow(id)
                    Log.d("Log", "$id $position $address")
                }
            }
        })
    }

    override fun openEstate(bundle: Bundle) {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_mainFragment_to_viewFragment, bundle)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_edit_property).isVisible = false
        menu.findItem(R.id.action_add_property).isVisible = true
        menu.findItem(R.id.action_exit).isVisible = true
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_exit -> {
                activity?.let { it ->
                    AlertDialog.Builder(it).apply {
                        setTitle("Are you sure?")
                        setPositiveButton("Yes") { _, _ ->
                            Helpers.signOut()
                            view?.let {
                                Navigation.findNavController(it)
                                    .navigate(R.id.action_mainFragment_to_loginFragment)
                            }
                        }
                        setNegativeButton("Cancel") { _, _ ->
                        }
                    }.create().show()
                }
                return true
            }
            R.id.action_add_property -> {
                view?.let {
                    Navigation.findNavController(it)
                        .navigate(R.id.action_mainFragment_to_addFragment)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}


