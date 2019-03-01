package com.justin.apps.wheretowatch.base

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.justin.apps.wheretowatch.model.Model.Media
import com.justin.apps.wheretowatch.repository.MediaRepository
import io.reactivex.disposables.Disposable

class SharedViewModel(val repo: MediaRepository): ViewModel() {
    private val TAG = "SharedViewModel"
    private var disposable: Disposable? = null

    lateinit var favoriteList: LiveData<List<Media>>

    init {
        load()
    }

    fun load() {
        favoriteList = repo.loadFromDatabase()
    }

    fun insert(media: Media?) {
        repo.insert(media)
    }

    fun remove(media: Media?) {
        repo.remove(media)
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}

class SharedViewModelFactory(private val repo: MediaRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SharedViewModel(repo) as T
    }
}