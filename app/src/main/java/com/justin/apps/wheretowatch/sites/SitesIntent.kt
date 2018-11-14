package com.justin.apps.wheretowatch.sites

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import com.justin.apps.wheretowatch.util.constants.CLASS_NAME_NETFLIX
import com.justin.apps.wheretowatch.util.constants.PACKAGE_NAME_NETFLIX
import com.justin.apps.wheretowatch.util.constants.SITE_NETFLIX
import java.lang.Exception

object SitesIntent {

    private var intent: Intent? = null

    fun buildIntent(site: String , url:String): SitesIntent {
        return when (site) {
            SITE_NETFLIX -> netflix(url)
            else -> genericSite(url)
        }
    }

    fun netflix(url: String): SitesIntent {
        try {
            intent = Intent(Intent.ACTION_VIEW)
            intent?.setClassName(PACKAGE_NAME_NETFLIX, CLASS_NAME_NETFLIX)
            intent?.data = Uri.parse(url)
            intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        } catch (e: Exception) {
            intent = Intent(Intent.ACTION_VIEW)
            intent?.data = Uri.parse(url)
        }
        return this
    }

    fun genericSite(url: String): SitesIntent {
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