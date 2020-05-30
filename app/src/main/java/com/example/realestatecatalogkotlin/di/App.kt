package com.example.realestatecatalogkotlin.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.View
import com.example.realestatecatalogkotlin.database.EstateDatabase
import com.example.realestatecatalogkotlin.database.EstateDbDao

class App : Application() {
    companion object {
        lateinit var estateDatabase: EstateDatabase
        lateinit var estateDb: EstateDbDao
        lateinit var appContext: Context
        lateinit var activity: Activity
        lateinit var layout: View
    }

    override fun onCreate() {
        super.onCreate()
        estateDatabase = EstateDatabase.getInstance(applicationContext)
        estateDb = estateDatabase.estateDbDao
    }
}