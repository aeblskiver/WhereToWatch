package com.justin.apps.wheretowatch.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Log
import com.justin.apps.wheretowatch.model.Model.Media
import com.justin.apps.wheretowatch.repository.MediaRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 *  View Model for the search fragment.
 */
class SearchViewModel(val repo: MediaRepository) : ViewModel() {
    private val TAG = "SearchViewModel"

    var mediaList: MutableLiveData<List<Media>> = MutableLiveData()
    var loading = false
    var freshSearch = false
    var disposable: Disposable? = null

    fun searchMedia(country: String ,term: String)  {
        loading = true
        disposable = repo.getFromApi(country, term)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setList, ::onError)
    }

    private fun setList(list: List<Media>) {
        mediaList.postValue(list)
    }

    private fun onError(e: Throwable) {
        Log.d(TAG, "Error: ${e.message}")
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}

class SearchViewModelFactory(private val repo: MediaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SearchViewModel(repo) as T
    }

}
