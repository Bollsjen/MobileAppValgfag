package dk.bollsjen.wantedcats.repositories

import retrofit2.Call
import dk.bollsjen.wantedcats.models.Cat
import retrofit2.http.*

interface CatsService {
    @GET("cats")
    fun getAllCats(): Call<List<Cat>>

    @GET("cats/{catId}")
    fun getCatById(@Path("catId") catId: Int):Call<Cat>

    @GET("cats")
    fun getSorted(@Query("userId") email: String):Call<List<Cat>>

    @GET("cats")
    fun getByPlace(@Query("place") place: String?): Call<List<Cat>>

    @GET("cats")
    fun getSortByReward(@Query("reward") reward: String):Call<List<Cat>>

    @POST("cats")
    fun saveCat(@Body cat: Cat): Call<Cat>

    @DELETE("cats/{id}")
    fun deleteCat(@Path("id") id: Int): Call<Cat>

    @PUT("cats{id}")
    fun updateCat(@Path("id") id: Int, @Body cat: Cat): Call<Cat>
}