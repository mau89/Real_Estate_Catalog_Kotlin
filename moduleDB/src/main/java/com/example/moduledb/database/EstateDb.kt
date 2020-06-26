package com.example.moduledb.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EstateDb(

    val photo: String,
    val address: String,
    val area: Double,
    val price: Double,
    val quantity_room: Int,
    val floor: Int,
    val price_sqm: Double
) {
    @PrimaryKey(autoGenerate = true)
    var estateId: Long = 0
}