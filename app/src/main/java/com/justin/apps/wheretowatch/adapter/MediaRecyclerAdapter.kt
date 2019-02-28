package com.justin.apps.wheretowatch.adapter

import android.app.ActionBar
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.transition.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.bumptech.glide.request.RequestOptions
import com.justin.apps.wheretowatch.R
import com.justin.apps.wheretowatch.model.Model
import kotlinx.android.synthetic.main.item_media.view.*

class MediaRecyclerAdapter(var rvListener: RecyclerViewFavoriteClickListener?, var isFavoriteFragment: Boolean = false) : RecyclerView.Adapter<MediaRecyclerAdapter.ListViewHolder>() {

    private val TAG = "MediaRecyclerAdapter"
    private var list: List<Model.Media> = emptyList()
    private lateinit var rv: RecyclerView

    fun setList(list: List<Model.Media>) {
        this.list = emptyList()
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

        // Needed to prevent imageviews from duplicating
        // TODO: Find a better fix
        holder.linearLayoutLocations.removeAllViews()

        list[position].locations.forEach {
            setAndInsertImageResource(holder, it)
        }
    }

    /*
        Uses Glide to set image resource and insert the image view into the Linear Layout
     */
    private fun setAndInsertImageResource(holder: ListViewHolder, it: Model.Location) {
        val icon = it.icon
        val image = ImageView(holder.context)
        image.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 128, 1f)
        image.setPadding(0,8,0,8)
        image.setOnClickListener(SiteClickListener(it))
        Glide.with(image.context)
            .load(icon)
            .into(image)
        holder.linearLayoutLocations.addView(image)
    }

    interface RecyclerViewFavoriteClickListener {
        fun onClick(view: View?, position: Int, favorite: Boolean)
    }

    inner class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val context: Context = view.context
        val tvMediaName: TextView = view.tv_media_name
        val tvMediaAvailableOn: TextView = view.tv_available_on
        val ivMediaView: ImageView = view.iv_media_pic
        val ivFavorite: ImageView = view.iv_favorite
        val linearLayoutLocations: LinearLayout = view.linearlayout_locations
        val clSubCard: ConstraintLayout = view.cl_subcard
        var isFavorite = false
        val imageResource: Int
            get() {
                return when (isFavorite) {
                    true -> R.drawable.ic_baseline_favorite_24px
                    else -> R.drawable.ic_baseline_favorite_border_24px
                }
            }

        init {
            ivMediaView.setOnClickListener{

                val changeBounds = ChangeBounds()
                changeBounds.addListener(object: Transition.TransitionListener {
                    override fun onTransitionEnd(transition: Transition?) {
                        val smoothScroller: RecyclerView.SmoothScroller = object: LinearSmoothScroller(context) {
                            @Override
                            override fun getVerticalSnapPreference(): Int {
                                return when (adapterPosition) {
                                    itemCount - 1 -> LinearSmoothScroller.SNAP_TO_END
                                    else -> LinearSmoothScroller.SNAP_TO_START
                                }
                            }
                        }
                        smoothScroller.targetPosition = adapterPosition
                        rv.layoutManager?.startSmoothScroll(smoothScroller)
                    }

                    override fun onTransitionResume(transition: Transition?) {}

                    override fun onTransitionPause(transition: Transition?) {}

                    override fun onTransitionCancel(transition: Transition?) {}

                    override fun onTransitionStart(transition: Transition?) {}

                })
                TransitionManager.go(Scene(rv), changeBounds)
                clSubCard.apply {
                    visibility = when (visibility) {
                        View.VISIBLE -> View.GONE
                        View.GONE -> View.VISIBLE
                        else -> View.VISIBLE
                    }

                }
            }

            ivFavorite.setImageResource(imageResource)

            ivFavorite.setOnClickListener {
                isFavorite = !isFavorite
                ivFavorite.setImageResource(imageResource)
                onClick(it)
            }
        }

        fun onClick(v: View?) {
            rvListener?.onClick(v, adapterPosition, isFavorite)
        }
    }
}
