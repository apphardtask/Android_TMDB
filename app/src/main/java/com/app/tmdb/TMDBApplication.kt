package com.app.tmdb

import android.app.Application
import com.app.tmdb.di.ApplicationComponent
import com.app.tmdb.di.DaggerApplicationComponent

class TMDBApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}