package dk.bollsjen.wantedcats.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import dk.bollsjen.wantedcats.models.Cat
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class CatRepository {
    private val url = "https://anbo-restlostcats.azurewebsites.net/api/"

    private val catService : CatsService
    val catsLiveData : MutableLiveData<List<Cat>> =MutableLiveData<List<Cat>>()
    val errorMessageLiveData : MutableLiveData<String> = MutableLiveData()
    val updateMessageLiveData : MutableLiveData<String> = MutableLiveData()

    init {
        val build: Retrofit = Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build()
        catService = build.create(CatsService::class.java)
        getPosts()
    }

    fun getPosts(){
        catService.getAllCats().enqueue(object: Callback<List<Cat>>{
            override fun onFailure(call: Call<List<Cat>>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("FISKERLARS", t.message!!)
            }

            override fun onResponse(call: Call<List<Cat>>, response: Response<List<Cat>>) {
                if(response.isSuccessful){
                    val b: List<Cat>? =response.body()
                    catsLiveData.postValue(b)
                    errorMessageLiveData.postValue("")
                }else{
                    val message =response.code().toString() + " " +response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("FISKERLARS", message)
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