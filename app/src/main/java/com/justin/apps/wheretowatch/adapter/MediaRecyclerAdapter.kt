package com.justin.apps.wheretowatch.adapter

import android.content.Context
import android.media.Image
import android.support.v7.widget.RecyclerView
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
import com.justin.apps.wheretowatch.network.MediaResponse
import io.reactivex.Observable
import kotlinx.android.synthetic.main.item_media.view.*

class ListRecyclerAdapter(val list :List<Model.Media>) : RecyclerView.Adapter<ListRecyclerAdapter.ListViewHolder>() {
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

        holder.ivMediaView.setOnClickListener {
            holder.tvMediaAvailableOn.also {
                it.visibility = if (it.visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }

        Observable.fromIterable(list[position].locations)
            .map {
                it.name to it.icon
//                it.name + ',' + it.icon
            }
            .toList()
            .subscribe{ s ->
                val (name, icon) = s
                val image = ImageView(holder.context)
                Glide.with(image)
                    .load(icon)
                    .into(image)
                holder.linearLayoutLocations.addView(image)
                holder.tvMediaAvailableOn.text = holder.itemView.context.getString(R.string.availableOn, name)

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
