package com.example.realestatecatalogkotlin.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EstateDbDao {
    @Insert
    suspend fun insertEstate(estate: EstateDb): Long

    @Update
    suspend fun updateEstate(estate: EstateDb)

    @Query("SELECT * from EstateDb WHERE estateId = :key")
    suspend fun getEstate(key: Long): EstateDb

    @Query("DELETE FROM EstateDb WHERE estateId = :key")
    suspend fun deleteEstate(key: Long)

    @Query("SELECT COUNT(*) FROM EstateDb")
    suspend fun getCount(): Long

    @Query("SELECT * FROM EstateDb ORDER BY estateId ASC")
    suspend fun getAllItems(): List<EstateDb>
}