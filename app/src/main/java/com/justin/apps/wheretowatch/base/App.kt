package com.justin.apps.wheretowatch.base

import android.app.Application

/**
 *  Application subclass used to get easy access to application context
 */
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