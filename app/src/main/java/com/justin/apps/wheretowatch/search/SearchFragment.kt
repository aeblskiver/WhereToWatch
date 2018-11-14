package com.justin.apps.wheretowatch.search

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.SearchView.OnQueryTextListener
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import com.justin.apps.wheretowatch.R
import com.justin.apps.wheretowatch.adapter.MediaRecyclerAdapter
import com.justin.apps.wheretowatch.repository.MediaRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.android.synthetic.main.search_fragment.view.*


class SearchFragment : Fragment() {
    private val TAG = "SearchFragment"

    private val repo: MediaRepository = MediaRepository
    private lateinit var  recyclerView: RecyclerView
    private lateinit var recyclerAdapter: MediaRecyclerAdapter
    private lateinit var loadingIndicator: ProgressBar
    private var disposable: Disposable? = null

    companion object {

        fun newInstance() = SearchFragment()
    }
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // Set title for the fragment
        activity?.setTitle(R.string.title_search)

        //Say there is an options menu for app bar
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this, SearchViewModelFactory(repo)).get(SearchViewModel::class.java)

        if (savedInstanceState == null) {
            viewModel.freshSearch = true
        }

        retainInstance = true

        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        setRecyclerAdapaterList()
        // TODO: Fix bug where locations get double when going back to activity
    }



    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return  inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadingIndicator = view.loading_indicator
        recyclerView = view.recyclerview_media_list
        recyclerAdapter = MediaRecyclerAdapter()
        recyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(context)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search_menu, menu)
        val searchItem = menu?.findItem(R.id.search_view)
        val searchView = searchItem?.actionView as SearchView
        if (viewModel.freshSearch) {
            Log.d(TAG, "Fresh search: ${viewModel.freshSearch}")
            searchItem.expandActionView()
        }

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                viewModel.freshSearch = false
                showLoading()
                searchQuery(s)
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                return true
            }

        } )

        //super.onCreateOptionsMenu(menu, inflater)
    }

    fun searchQuery(s: String?) {
        viewModel.searchMedia("us", s ?: "")
        setRecyclerAdapaterList()
    }

    private fun showLoading() {
        loadingIndicator.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun hideLoading() {
        loadingIndicator.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun setRecyclerAdapaterList() {
        disposable = viewModel.mediaList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                recyclerAdapter.setList(list)
                hideLoading()
            }
    }
}