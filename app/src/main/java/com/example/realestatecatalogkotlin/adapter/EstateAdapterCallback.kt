package com.example.realestatecatalogkotlin.adapter

interface EstateAdapterCallback {
    fun openItem(id: Long)
    fun deleteRow(id: Long, position: Int, address: String)
}