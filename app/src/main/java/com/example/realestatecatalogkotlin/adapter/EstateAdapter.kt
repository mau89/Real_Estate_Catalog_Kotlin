package com.example.realestatecatalogkotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moduledb.database.EstateDb
import com.example.realestatecatalogkotlin.R

class EstateAdapter :
    RecyclerView.Adapter<EstateViewHolder>() {
    private val estateList: MutableList<EstateDb> = ArrayList()
    var delegate: EstateAdapterCallback? = null

    fun attachDelegate(delegate: EstateAdapterCallback) {
        this.delegate = delegate
    }

    fun setData(newEstateList: List<EstateDb>) {
        estateList.clear()
        estateList.addAll(newEstateList)
        notifyDataSetChanged()
    }

    fun deleteRow(position: Int) {
        estateList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): EstateViewHolder {
        return EstateViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.property_rv_item, viewGroup, false), delegate
        )
    }

    override fun getItemCount(): Int {
        return estateList.count()
    }

    override fun onBindViewHolder(holder: EstateViewHolder, position: Int) {
        holder.bind(estateList[position])
    }
}



