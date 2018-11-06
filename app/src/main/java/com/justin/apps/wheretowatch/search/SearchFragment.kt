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
import android.view.*
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
    private val repo: MediaRepository = MediaRepository
    private lateinit var  recyclerView: RecyclerView
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
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return  inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.recyclerview_media_list
        val adapter = MediaRecyclerAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, SearchViewModelFactory(repo)).get(SearchViewModel::class.java)
        disposable = viewModel.mediaList
            ?.observeOn(Schedulers.io())
            ?.subscribeOn(AndroidSchedulers.mainThread())
            ?.subscribe { s ->
                (recyclerView.adapter as MediaRecyclerAdapter).setList(s)
                (recyclerView.adapter as MediaRecyclerAdapter).notifyDataSetChanged()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search_menu, menu)
//        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE)
        val searchView = menu?.findItem(R.id.search_view)?.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
                viewModel.searchMedia("us", s ?: "")
                recyclerView.adapter?.notifyDataSetChanged()
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                return true
            }

        } )

        //super.onCreateOptionsMenu(menu, inflater)
    }
}