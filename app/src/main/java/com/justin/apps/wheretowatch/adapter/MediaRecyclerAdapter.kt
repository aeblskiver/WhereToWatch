package com.justin.apps.wheretowatch.adapter

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.ActionBarOverlayLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.justin.apps.wheretowatch.R
import com.justin.apps.wheretowatch.model.Model
import com.justin.apps.wheretowatch.util.constants.CLASS_NAME_NETFLIX
import com.justin.apps.wheretowatch.util.constants.PACKAGE_NAME_NETFLIX
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.android.synthetic.main.item_media.view.*
import java.lang.Exception

class MediaRecyclerAdapter : RecyclerView.Adapter<MediaRecyclerAdapter.ListViewHolder>() {

    private var list: List<Model.Media> = mutableListOf()
    private lateinit var rv: RecyclerView

    fun setList(list: List<Model.Media>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        rv = recyclerView
    }

    /*
            Called when RecyclerView needs a new ViewHolder of the given type to represent an item
            @param parent - Parent viewgroup
            @param viewType -
         */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_media, parent, false)

        return ListViewHolder(view)
    }

    override fun getItemCount() = list.size

    /*
        Called by RecyclerView to display data at the specified position
     */
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.tvMediaName.text = list[position].name

        Glide.with(holder.ivMediaView.context)
            .load(list[position].picture)
            .apply(RequestOptions.fitCenterTransform())
            .into(holder.ivMediaView)

        holder.ivMediaView.setOnClickListener { _ ->

            TransitionManager.beginDelayedTransition(rv, AutoTransition())
            holder.let { it ->
                it.tvMediaAvailableOn.visibility = if (it.tvMediaAvailableOn.visibility == View.GONE) View.VISIBLE else View.GONE
                it.linearLayoutLocations.visibility = if (it.linearLayoutLocations.visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }

        list[position].locations.forEach {
            val name = it.name
            val icon = it.icon
            val displayName = it.displayName
            val url = it.url

            Log.d("MediaRecyclerAdapter", "Name: $name + Icon: $icon + Display Name: ${it.displayName}")

            val image = ImageView(holder.context)
            image.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            image.setOnClickListener {
                when (displayName) {
                    "Netflix" -> netflixIntent(url)
                }
            }
            Glide.with(image.context)
                .load(icon)
                .into(image)
            holder.linearLayoutLocations.addView(image)

        }
    }

    private fun netflixIntent(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setClassName(PACKAGE_NAME_NETFLIX, CLASS_NAME_NETFLIX)
            intent.data = Uri.parse(url)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(rv.context, intent, null)
        } catch (e: Exception) {
            Log.d("RecyclerAdapter", "Error: $e")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(rv.context, intent, null)
        }
    }

    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvMediaName: TextView = view.tv_media_name
        val tvMediaAvailableOn: TextView = view.tv_available_on
        val ivMediaView: ImageView = view.iv_media_pic
        val linearLayoutLocations: LinearLayout = view.linearlayout_locations
        val context: Context = view.context

    }

}
