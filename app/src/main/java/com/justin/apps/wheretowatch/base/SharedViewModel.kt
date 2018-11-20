package com.justin.apps.wheretowatch.base

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Room
import android.util.Log
import android.widget.Toast
import com.justin.apps.wheretowatch.db.MediaRoomDatabase
import com.justin.apps.wheretowatch.db.RoomDao
import com.justin.apps.wheretowatch.model.Model
import com.justin.apps.wheretowatch.model.Model.Media
import com.justin.apps.wheretowatch.repository.MediaRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SharedViewModel(val repo: MediaRepository): ViewModel() {
    private val TAG = "SharedViewModel"
    private var disposable: Disposable? = null

    lateinit var favoriteList: LiveData<List<Media>>

    init {
        load()
    }

    fun load() {
//        disposable = repo.loadFromDatabase()
//            ?.subscribeOn(Schedulers.io())
//            ?.observeOn(AndroidSchedulers.mainThread())
//            ?.subscribe(::onSuccess, ::onError)
        favoriteList = repo.loadFromDatabase()
    }

    suspend fun insert(media: Media?) {
        repo.insert(media)
    }

    fun remove(media: Media) {
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