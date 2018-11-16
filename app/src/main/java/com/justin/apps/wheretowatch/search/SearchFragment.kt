package com.justin.apps.wheretowatch.search

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import com.justin.apps.wheretowatch.R
import com.justin.apps.wheretowatch.adapter.MediaRecyclerAdapter
import com.justin.apps.wheretowatch.db.MediaRoomDatabase
import com.justin.apps.wheretowatch.repository.MediaRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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

        savedInstanceState?.let {
            if (it.getBoolean("freshSearch")) {
                viewModel.freshSearch = true
            }
        }
        retainInstance = true

        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        setRecyclerAdapterList()
    }

    override fun onPause() {
        super.onPause()
        viewModel.freshSearch = false
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
        recyclerAdapter = MediaRecyclerAdapter(object : MediaRecyclerAdapter.RecyclerViewFavoriteClickListener  {
            override fun onClick(view: View?, position: Int) {
                Toast.makeText(context, "Position: $position", Toast.LENGTH_SHORT).show()

//                context?.let {
//                    val cx = it
//                    MediaRoomDatabase.getInstance(cx)?.roomDao()?.insertMedia()
//                }


            }
        })
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
                searchView.clearFocus()
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
        setRecyclerAdapterList()
    }

    private fun showLoading() {
        loadingIndicator.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun hideLoading() {
        loadingIndicator.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun setRecyclerAdapterList() {
        disposable = viewModel.mediaList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                recyclerAdapter.setList(list)
                hideLoading()
            }
    }
}
