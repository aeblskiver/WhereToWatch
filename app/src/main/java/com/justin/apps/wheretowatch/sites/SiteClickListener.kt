package com.justin.apps.wheretowatch.sites

import android.view.View
import com.justin.apps.wheretowatch.model.Model.Location

/**
 *  Listener for clicks on a streaming site that builds and launches an intent.
 */
class SiteClickListener(private val site: Location): View.OnClickListener {

    override fun onClick(v: View?) {
        v?.let {
            SitesIntent.buildIntent(site.url).launch(it.context)
        }
    }

}