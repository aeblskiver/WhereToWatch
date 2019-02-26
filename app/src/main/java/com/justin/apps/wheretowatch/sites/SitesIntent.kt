package com.justin.apps.wheretowatch.sites

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import com.justin.apps.wheretowatch.util.constants.CLASS_NAME_NETFLIX
import com.justin.apps.wheretowatch.util.constants.PACKAGE_NAME_NETFLIX
import com.justin.apps.wheretowatch.util.constants.SITE_NETFLIX

object SitesIntent {

    private var intent: Intent? = null

    fun buildIntent(url:String): SitesIntent {
        return genericSite(url)
    }

    private fun genericSite(url: String): SitesIntent {
        intent = Intent(Intent.ACTION_VIEW)
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent?.data = Uri.parse(url)

        return this
    }

    fun launch(context: Context) {
        intent?.let {
            startActivity(context, it, null)
        }
    }
}