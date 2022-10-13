package dk.bollsjen.wantedcats.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dk.bollsjen.wantedcats.databinding.FragmentFirstBinding
import dk.bollsjen.wantedcats.repositories.CatRepository
import java.util.*

class CatsViewModel : ViewModel() {
    var repository = CatRepository()
    var catsLiveData: LiveData<List<Cat>> = repository.catsLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData
    val updateMessageLiveData: LiveData<String> = repository.updateMessageLiveData

    var sortDate: Int = 0
    var sortName: Int = 0
    var sortPlace: Int = 0
    var sortReward: Int = 0

    init {
        reload()
    }

    fun reload() {
        repository.getPosts()
    }

    fun myCats(email: String?){
        if(email != null)
            repository.getMyCats(email)
    }

    fun getPlace(place: String?){
        if(place != "")
            repository.getPlace(place)
        else reload()
    }

    fun resetSorting(list: List<Cat>){
        //repository.sortByList(list)

        catsLiveData = repository.catsLiveData


    }

    fun getRewardUpperLimit(): Int {
        var limit: Int = 0
        for(item in repository.catsLiveData.value!!){
            if(item.reward > limit){
                limit = item.reward
            }
        }

        return limit
    }

    fun getRewardLowerLimit(): Int {
        var limit: Int = Int.MAX_VALUE
        for(item in repository.catsLiveData.value!!){
            if(item.reward < limit){
                limit = item.reward
            }
        }

        return limit
    }

    fun filterByRewards(lower: Int, upper: Int){
        var list: List<Cat>? = null

        list = catsLiveData.value!!.filter { it.reward >= lower && it.reward <= upper}

        repository.sortByList(list)
    }

    fun sortList(){
        var list: List<Cat>? = null

        if(sortDate == 1) {
            list = catsLiveData.value!!.sortedWith(Comparator({ x, y -> y.date.compareTo(x.date) }))
        }else if(sortDate == 2){
            list = catsLiveData.value!!.sortedWith(Comparator({ x, y -> x.date.compareTo(y.date) }))
        }else{
            resetSorting(repository.catsOriginalData.value!!)
        }
        if(sortName == 1){
            list = catsLiveData.value!!.sortedWith(Comparator({ x, y -> x.name.compareTo(y.name) }))
        }else if(sortName == 2){
            list = catsLiveData.value!!.sortedWith(Comparator({ x, y -> y.name.compareTo(x.name) }))
        }else{
            resetSorting(repository.catsOriginalData.value!!)
        }
        if(sortPlace == 1){
            list = catsLiveData.value!!.sortedWith(Comparator({ x, y -> y.place.compareTo(x.place) }))
        }else if(sortPlace == 2){
            list = catsLiveData.value!!.sortedWith(Comparator({ x, y -> x.place.compareTo(y.place) }))
        }else{
            resetSorting(repository.catsOriginalData.value!!)
        }
        if(sortReward == 1){
            list = catsLiveData.value!!.sortedWith(Comparator({ x, y -> y.reward.compareTo(x.reward) }))
        }else if(sortReward == 2){
            list = catsLiveData.value!!.sortedWith(Comparator({ x, y -> x.reward.compareTo(y.reward) }))
        }else{
            resetSorting(repository.catsOriginalData.value!!)
        }

        repository.sortByList(list)
    }


    operator fun get(index: Int): Cat? {
        return catsLiveData.value?.get(index)
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