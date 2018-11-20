package com.justin.apps.wheretowatch.adapter

import android.app.Application

class App: Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: App? = null

        fun appContext(): App {
            if (instance == null) {
                instance = App()
            }
            return instance as App
        }
    }
}