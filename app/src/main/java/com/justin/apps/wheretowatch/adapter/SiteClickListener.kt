package com.justin.apps.wheretowatch.adapter

import android.view.View
import com.justin.apps.wheretowatch.model.Model.Location
import com.justin.apps.wheretowatch.sites.SitesIntent


class SiteClickListener(private val site: Location): View.OnClickListener {

    override fun onClick(v: View?) {
        v?.let {
            SitesIntent.buildIntent(site.displayName, site.url).launch(it.context)
        }
    }

}