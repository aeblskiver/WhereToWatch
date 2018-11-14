package com.justin.apps.wheretowatch.sites

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import com.justin.apps.wheretowatch.util.constants.CLASS_NAME_NETFLIX
import com.justin.apps.wheretowatch.util.constants.PACKAGE_NAME_NETFLIX
import java.lang.Exception

enum class Site {
    NETFLIX,
    AMAZON,
    ITUNES,
}

object SitesIntent {

    private var intent: Intent? = null

    fun netflix(url: String): SitesIntent {
        try {
            intent = Intent(Intent.ACTION_VIEW)
            intent?.setClassName(PACKAGE_NAME_NETFLIX, CLASS_NAME_NETFLIX)
            intent?.data = Uri.parse(url)
            intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        } catch (e: Exception) {
            Log.d("RecyclerAdapter", "Error: $e")
            intent = Intent(Intent.ACTION_VIEW)
            intent?.data = Uri.parse(url)
        }
        return this
    }

    fun launch(context: Context) {
        intent?.let {
            startActivity(context, it, null)
        }
    }

    fun amazon(url: String): SitesIntent {
        intent = Intent(Intent.ACTION_VIEW)
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent?.data = Uri.parse(url)

        return this
    }


}