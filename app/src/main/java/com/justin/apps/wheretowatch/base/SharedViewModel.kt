package com.justin.apps.wheretowatch.base

import android.arch.lifecycle.*
import com.bumptech.glide.Glide.init
import com.justin.apps.wheretowatch.filter.FilterDialogFragment
import com.justin.apps.wheretowatch.model.Model.Media
import com.justin.apps.wheretowatch.repository.MediaRepository
import com.justin.apps.wheretowatch.util.constants.AMAZON_INSTANT_DISPLAY_NAME
import com.justin.apps.wheretowatch.util.constants.AMAZON_PRIME_DISPLAY_NAME
import com.justin.apps.wheretowatch.util.constants.NETFLIX_DISPLAY_NAME
import io.reactivex.disposables.Disposable
import java.util.Locale.filter

class SharedViewModel(val repo: MediaRepository): ViewModel() {
    private var disposable: Disposable? = null
    private var choiceSet: MutableLiveData<Set<String>> = MutableLiveData()
    lateinit var favoriteList: LiveData<List<Media>>
    var filteredList: LiveData<List<Media>> = Transformations.switchMap(choiceSet) { choices -> filterList(choices) }



    init {
        load()
        choiceSet.postValue(setOf(NETFLIX_DISPLAY_NAME, AMAZON_INSTANT_DISPLAY_NAME, AMAZON_PRIME_DISPLAY_NAME))

    }

    private fun filterList(choiceSet: Set<String>): LiveData<List<Media>> {
        val result = MutableLiveData<List<Media>>()
        val list = favoriteList.value
        result.value = list?.filter { media ->
            var found = false
            media.locations.forEach { locations ->
                if (choiceSet.contains(locations.name)) found = true
            }
            found
        }
        return result
    }


    fun load() {
//        favoriteList = repo.loadFromDatabase()
        filteredList = repo.loadFromDatabase()
    }

    fun insert(media: Media?) {
        repo.insert(media)
    }

    fun remove(media: Media?) {
        repo.remove(media)
    }

    fun setFilter(filter: Set<String>) {
        choiceSet.setValue(filter)
    }


    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}

class SharedViewModelFactory(private val repo: MediaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SharedViewModel(repo) as T
    }
}