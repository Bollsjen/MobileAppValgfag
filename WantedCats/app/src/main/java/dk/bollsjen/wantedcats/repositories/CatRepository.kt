package dk.bollsjen.wantedcats.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dk.bollsjen.wantedcats.models.Cat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class CatRepository {
    private val url = "https://anbo-restlostcats.azurewebsites.net/api/"

    private val catService : CatsService
    var catsLiveData : MutableLiveData<List<Cat>> =MutableLiveData<List<Cat>>()
    var catsOriginalData: MutableLiveData<List<Cat>> = MutableLiveData<List<Cat>>()
    val errorMessageLiveData : MutableLiveData<String> = MutableLiveData()
    val updateMessageLiveData : MutableLiveData<String> = MutableLiveData()

    init {
        val build: Retrofit = Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build()
        catService = build.create(CatsService::class.java)
        getPosts()
    }

    fun getPosts() = GlobalScope.async{
        catService.getAllCats().enqueue(object: Callback<List<Cat>>{
            override fun onFailure(call: Call<List<Cat>>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("FISKERLARS1", t.message!!)
            }

            override fun onResponse(call: Call<List<Cat>>, response: Response<List<Cat>>) {
                if(response.isSuccessful){
                    val b: List<Cat>? =response.body()
                    catsLiveData.postValue(b)
                    catsOriginalData.postValue(b)
                    errorMessageLiveData.postValue("")
                }else{
                    val message =response.code().toString() + " " +response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("FISKERLARS1", message)
                }
            }
        })
    }

    fun returnPosts() : Sequence<List<Cat>> = sequence  {
        var list: List<Cat> = emptyList()
        catService.getAllCats().enqueue(object: Callback<List<Cat>>{
            override fun onFailure(call: Call<List<Cat>>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                list = emptyList<Cat>()
                Log.d("FISKERLARS1", t.message!!)
            }

            override fun onResponse(call: Call<List<Cat>>, response: Response<List<Cat>>) {
                if(response.isSuccessful){
                    val b: List<Cat>? =response.body()
                    list = b!!
                    Log.d("S??REN", b.toString())
                    errorMessageLiveData.postValue("")
                }else{
                    val message =response.code().toString() + " " +response.message()
                    errorMessageLiveData.postValue(message)
                    list = emptyList<Cat>()
                    Log.d("FISKERLARS1", message)
                }
            }
        })

        yield (list)
    }

    fun sortByList(b: List<Cat>?){
        if(b != null){
            catsLiveData.postValue(b)
            errorMessageLiveData.postValue("")
        }else{
            errorMessageLiveData.postValue("")
        }
    }

    fun getMyCats(email: String){
        catService.getSorted(email).enqueue(object: Callback<List<Cat>>{
            override fun onFailure(call: Call<List<Cat>>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("FISKERLARS2", t.message!!)
            }

            override fun onResponse(call: Call<List<Cat>>, response: Response<List<Cat>>) {
                if(response.isSuccessful){
                    val b: List<Cat>? =response.body()
                    catsLiveData.postValue(b)
                    catsOriginalData.postValue(b)
                    errorMessageLiveData.postValue("")
                }else{
                    val message =response.code().toString() + " " +response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("FISKERLAR2", response.toString())
                }
            }
        })
    }

    fun getSorted(filter: String){
        catService.getByPlace(filter).enqueue(object: Callback<List<Cat>>{
            override fun onFailure(call: Call<List<Cat>>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("FISKERLARS3", t.message!!)
            }

            override fun onResponse(call: Call<List<Cat>>, response: Response<List<Cat>>) {
                if(response.isSuccessful){
                    val b: List<Cat>? =response.body()
                    catsLiveData.postValue(b)
                    errorMessageLiveData.postValue("")
                }else{
                    val message =response.code().toString() + " " +response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("FISKERLARS3", response.toString())
                }
            }
        })
    }

    fun getPlace(place: String?){
        catService.getByPlace(place).enqueue(object: Callback<List<Cat>>{
            override fun onFailure(call: Call<List<Cat>>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("FISKERLARS4", t.message!!)
            }

            override fun onResponse(call: Call<List<Cat>>, response: Response<List<Cat>>) {
                if(response.isSuccessful){
                    val b: List<Cat>? =response.body()
                    catsLiveData.postValue(b)
                    errorMessageLiveData.postValue("")
                }else{
                    val message =response.code().toString() + " " +response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("FISKERLARS4", response.toString())
                }
            }
        })
    }

    fun sortByReward(){
        catService.getSortByReward("reward").enqueue(object: Callback<List<Cat>>{
            override fun onFailure(call: Call<List<Cat>>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("FISKERLARS4", t.message!!)
            }

            override fun onResponse(call: Call<List<Cat>>, response: Response<List<Cat>>) {
                if(response.isSuccessful){
                    val b: List<Cat>? =response.body()
                    catsLiveData.postValue(b)
                    errorMessageLiveData.postValue("")
                }else{
                    val message =response.code().toString() + " " +response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("FISKERLARS4", response.toString())
                }
            }
        })
    }

    fun add(cat: Cat) {
        catService.saveCat(cat).enqueue(object : Callback<Cat> {
            override fun onResponse(call: Call<Cat>, response: Response<Cat>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Added: " + response.body())
                    updateMessageLiveData.postValue("Added: " + response.body())
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<Cat>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun delete(id: Int) {
        catService.deleteCat(id).enqueue(object : Callback<Cat> {
            override fun onResponse(call: Call<Cat>, response: Response<Cat>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Deleted: " + response.body())
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<Cat>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun update(cat: Cat) {
        catService.updateCat(cat.id, cat).enqueue(object : Callback<Cat> {
            override fun onResponse(call: Call<Cat>, response: Response<Cat>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Updated: " + response.body())
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<Cat>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }
}