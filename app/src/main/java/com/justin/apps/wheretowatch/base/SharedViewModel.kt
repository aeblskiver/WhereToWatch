package com.justin.apps.wheretowatch.base

import android.arch.lifecycle.*
import com.justin.apps.wheretowatch.model.Model.Media
import com.justin.apps.wheretowatch.repository.MediaRepository
import com.justin.apps.wheretowatch.util.constants.AMAZON_INSTANT_DISPLAY_NAME
import com.justin.apps.wheretowatch.util.constants.AMAZON_PRIME_DISPLAY_NAME
import com.justin.apps.wheretowatch.util.constants.NETFLIX_DISPLAY_NAME
import io.reactivex.disposables.Disposable

class SharedViewModel(val repo: MediaRepository): ViewModel() {
    private var disposable: Disposable? = null
    private var choiceSet: MutableLiveData<Set<String>> = MutableLiveData()
    private var favoriteList: LiveData<List<Media>> = repo.loadFromDatabase()
    private val filterTrigger = FilterTrigger<List<Media>, Set<String>>(favoriteList, choiceSet)

    init {
        load()
        choiceSet.postValue(setOf(NETFLIX_DISPLAY_NAME, AMAZON_INSTANT_DISPLAY_NAME, AMAZON_PRIME_DISPLAY_NAME))

    }

    var filteredList: LiveData<List<Media>> = Transformations.switchMap(filterTrigger) { choices -> filterList(choices) }

    private fun filterList(p: Pair<List<Media>?, Set<String>?>): LiveData<List<Media>> {
        val result = MutableLiveData<List<Media>>()
        if (p.first != null && p.second != null) {
            val filteredList = p.first!!.filter { media ->
                var found = false
                media.locations.forEach { l ->
                    if (p.second!!.contains(l.name)) found = true
                }
                found
            }


            result.postValue(filteredList)
        }

        return result
    }


    fun load() {
        favoriteList = repo.loadFromDatabase()
        //filteredList = repo.loadFromDatabase()
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

// Using generics to subclass mediator live data.
// Taken from https://stackoverflow.com/questions/49493772/mediatorlivedata-or-switchmap-transformation-with-multiple-parameters
class FilterTrigger<A, B>(a: LiveData<A>, b: LiveData<B>): MediatorLiveData<Pair<A?, B?>>() {
    init {
        addSource(a) { value = it to b.value }
        addSource(b) { value = a.value to it }
    }
}