package com.justin.apps.wheretowatch.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.util.Log
import android.view.View
import com.justin.apps.wheretowatch.adapter.MediaRecyclerAdapter
import com.justin.apps.wheretowatch.model.Model
import com.justin.apps.wheretowatch.repository.MediaRepository
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers

class SearchViewModel(val repo: MediaRepository) : ViewModel() {
    private val TAG = "SearchViewModel"

    var mediaList: Maybe<List<Model.Media>> = Maybe.just(emptyList<Model.Media>())
    lateinit var mediaList2: MutableLiveData<List<Model.Media>>
    var loading = false
    var freshSearch = false
    var disposable: Disposable? = null

    init {
//       val d = mediaList
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSuccess {
//                mediaList2 = it
//            }
        mediaList2 = MutableLiveData()
    }

    // TODO: Implement the ViewModel
    fun searchMedia(country: String ,term: String)  {
        loading = true
        disposable = repo.getFromApi(country, term)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setList, ::onError)
    }

    fun insertMedia(media: Model.Media) {

    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }

    private fun setList(list: List<Model.Media>) {
        Log.d(TAG, "List: $list")
        mediaList2.postValue(list)
    }

    private fun onError(e: Throwable) {
        Log.d(TAG, "Error: ${e.message}")
    }
}

class SearchViewModelFactory(private val repo: MediaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SearchViewModel(repo) as T
    }

}
