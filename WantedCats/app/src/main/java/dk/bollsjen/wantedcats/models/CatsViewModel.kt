package dk.bollsjen.wantedcats.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.bollsjen.wantedcats.repositories.CatRepository

class CatsViewModel : ViewModel() {
    private val repository = CatRepository()
    val booksLiveData: LiveData<List<Cat>> = repository.catsLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData
    val updateMessageLiveData: LiveData<String> = repository.updateMessageLiveData

    init {
        reload()
    }

    fun reload() {
        repository.getPosts()
    }

    operator fun get(index: Int): Cat? {
        return booksLiveData.value?.get(index)
    }

    fun add(book: Cat) {
        repository.add(book)
    }

    fun delete(id: Int) {
        repository.delete(id)
    }

    fun update(book: Cat) {
        repository.update(book)
    }
}