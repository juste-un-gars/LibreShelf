package com.libreshelf

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LibreShelfApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
