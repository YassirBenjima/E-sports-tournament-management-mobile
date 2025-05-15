package com.example.e_sports_tournament_management_system.network

import com.example.e_sports_tournament_management_system.model.Game
import com.example.e_sports_tournament_management_system.model.LoginRequest
import com.example.e_sports_tournament_management_system.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("/api/login")
    fun login(@Body loginRequest: LoginRequest): Call<ResponseBody>

    // users :
    @GET("/api/users")
    fun getUsers(): Call<List<User>>

    @POST("/api/users")
    fun addUser(@Body user: User): Call<User>

    @PUT("/api/users/{id}")
    fun updateUser(@Path("id") id: Long, @Body user: User): Call<User>

    @DELETE("/api/users/{id}")
    fun deleteUser(@Path("id") id: Long): Call<Void>


    // games :
    @GET("/api/games")
    fun getGames(): Call<List<Game>>

    @POST("/api/games")
    fun addGame(@Body game: Game): Call<Game>

    @PUT("/api/games/{id}")
    fun updateGame(@Path("id") id: Long, @Body game: Game): Call<Game>

    @DELETE("/api/games/{id}")
    fun deleteGame(@Path("id") id: Long): Call<Void>



}
