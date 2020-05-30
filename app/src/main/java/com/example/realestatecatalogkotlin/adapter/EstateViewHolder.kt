package com.example.realestatecatalogkotlin.adapter

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.realestatecatalogkotlin.R
import com.example.realestatecatalogkotlin.database.EstateDb

import com.squareup.picasso.Picasso
import java.io.File

class EstateViewHolder(itemView: View, private val delegate: EstateAdapterCallback?) :
    RecyclerView.ViewHolder(itemView) {
    private var ivPropertyItem: ImageView = itemView.findViewById(R.id.iv_property_item)
    private var tvPriceItem: TextView = itemView.findViewById(R.id.tv_price_item)
    private var tvAddressItem: TextView = itemView.findViewById(R.id.tv_address_item)
    private var tvAreaItem: TextView = itemView.findViewById(R.id.tv_area_item)
    private var btnDeleteEstate: Button = itemView.findViewById(R.id.btn_delete_estate)

    fun bind(item: EstateDb) {
        tvAddressItem.text = item.address
        Picasso.get().load(
            File(item.photo)
        ).into(ivPropertyItem)
        tvAreaItem.text = item.area.toBigDecimal().toPlainString()
        tvPriceItem.text = item.price.toBigDecimal().toPlainString()
        itemView.setOnClickListener {
            delegate?.openItem(item.estateId)
        }
        btnDeleteEstate.setOnClickListener {
            item.address.let { it1 -> delegate?.deleteRow(item.estateId, layoutPosition, it1) }
        }
    }
}