package com.justin.apps.wheretowatch.adapter

import android.view.View
import com.justin.apps.wheretowatch.model.Model
import com.justin.apps.wheretowatch.model.Model.Location
import com.justin.apps.wheretowatch.sites.Site
import com.justin.apps.wheretowatch.sites.Site.NETFLIX
import com.justin.apps.wheretowatch.sites.SitesIntent
import com.justin.apps.wheretowatch.util.constants.SITE_AMAZON
import com.justin.apps.wheretowatch.util.constants.SITE_NETFLIX


class SiteClickListener(private val site: Location): View.OnClickListener {

    override fun onClick(v: View?) {
        v?.let {
            when (site.displayName) {
                SITE_NETFLIX -> SitesIntent.netflix(site.url).launch(it.context)
                SITE_AMAZON -> SitesIntent.amazon(site.url).launch(it.context)
            }
        }
    }

}