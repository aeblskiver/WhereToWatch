package com.justin.apps.wheretowatch.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.findNavController
import com.justin.apps.wheretowatch.R
import com.justin.apps.wheretowatch.adapter.MediaRecyclerAdapter
import com.justin.apps.wheretowatch.base.BaseActivity
import com.justin.apps.wheretowatch.base.FavoriteClickListener
import com.justin.apps.wheretowatch.model.Model.Media
import com.justin.apps.wheretowatch.repository.MediaRepository
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.android.synthetic.main.search_fragment.view.*

/**
 *  Fragment where users can search for shows/movies. Uses the network API to make requests.
 */
class SearchFragment : Fragment() {
    private val TAG = "SearchFragment"

    private lateinit var  recyclerView: RecyclerView
    private lateinit var recyclerAdapter: MediaRecyclerAdapter
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var viewModel: SearchViewModel
    private lateinit var favoriteClickListener: FavoriteClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, SearchViewModelFactory(MediaRepository)).get(SearchViewModel::class.java)
        favoriteClickListener = activity as BaseActivity

        savedInstanceState?.let {
            if (it.getBoolean("freshSearch")) {
                viewModel.freshSearch = true
            }
        }
        retainInstance = true

        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }


    override fun onPause() {
        super.onPause()
        viewModel.freshSearch = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return  inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadingIndicator = view.loading_indicator
        setUpRecyclerView(view)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search_menu, menu)
        setUpSearchView(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setUpSearchView(menu: Menu?) {
        val searchItem = menu?.findItem(R.id.search_view)
        val searchView = searchItem?.actionView as SearchView

        (searchView.findViewById<EditText>(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(
            ContextCompat.getColor(
                this.context!!, R.color.appBarText
            )
        )

        if (viewModel.freshSearch) {
            searchItem.expandActionView()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                viewModel.freshSearch = false
                showLoading()
                searchQuery(s)
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                return true
            }
        })
    }

    private fun setUpRecyclerView(view: View) {
        recyclerView = view.recyclerview_media_list
        recyclerAdapter = MediaRecyclerAdapter(favoriteClickListener)
        recyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(context)
        }
        viewModel.mediaList2.observe(this, Observer {
            recyclerAdapter.setList(it ?: emptyList())
            recyclerAdapter.notifyDataSetChanged()
            hideLoading()
        })
    }

    fun searchQuery(s: String?) {
        viewModel.searchMedia("us", s ?: "")
    }

    private fun showLoading() {
        loadingIndicator.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun hideLoading() {
        loadingIndicator.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }
}
