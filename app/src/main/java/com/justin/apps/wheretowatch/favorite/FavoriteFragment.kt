package com.justin.apps.wheretowatch.favorite

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.justin.apps.wheretowatch.R
import com.justin.apps.wheretowatch.adapter.MediaRecyclerAdapter
import com.justin.apps.wheretowatch.base.BaseActivity
import kotlinx.android.synthetic.main.fragment_favorite.*

/**
 *  Fragment for displaying the user's favorite shows/movies. These items are saved in and pulled from the Room
 *  database.
 */
class FavoriteFragment : Fragment() {
    private val TAG = "FavoriteFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rv = recyclerview_favorite_list
        rv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MediaRecyclerAdapter(activity as BaseActivity, true)
        }
        (activity as BaseActivity).sharedViewModel.favoriteList.observe(this, Observer {
            Log.d(TAG, "List: $it")
            (rv.adapter as MediaRecyclerAdapter).setList(it ?: emptyList())
        })
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        (activity as BaseActivity).showAppBarLayout()
    }
}
