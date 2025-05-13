package com.example.e_sports_tournament_management_system.network

import com.example.e_sports_tournament_management_system.model.LoginRequest
import com.example.e_sports_tournament_management_system.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET

interface ApiService {

    @POST("/api/login")
    fun login(@Body loginRequest: LoginRequest): Call<ResponseBody>

    @GET("/api/users")
    fun getUsers(): Call<List<User>>

    @POST("/api/users")
    fun addUser(@Body user: User): Call<User>
}
