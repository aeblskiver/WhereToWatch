package com.justin.apps.wheretowatch.sites

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity

/**
 *  Class used to create and launch intents that would either navigate to a website where the user can watch
 *  a show/movie or it would open the correct app (Netflix app, Amazon Prime Video app, etc.)
 *
 *  Originally I thought this would be a lot more complicated as I thought I would have to find the right package names
 *  for each individual app to build the intents but it turns out simple implicit intents are able to launch the correct app
 *  based off the url alone.
 */
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