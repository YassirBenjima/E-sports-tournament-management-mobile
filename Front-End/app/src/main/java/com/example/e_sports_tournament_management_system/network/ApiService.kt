package com.example.e_sports_tournament_management_system.network

import com.example.e_sports_tournament_management_system.model.Game
import com.example.e_sports_tournament_management_system.model.LoginRequest
import com.example.e_sports_tournament_management_system.model.Match
import com.example.e_sports_tournament_management_system.model.Player
import com.example.e_sports_tournament_management_system.model.Team
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

    // players :

    @GET("/api/players")
    fun getPlayers(): Call<List<Player>>

    @POST("/api/players")
    fun addPlayer(@Body player: Player): Call<Player>

    @PUT("/api/players/{id}")
    fun updatePlayer(@Path("id") id: Long, @Body player: Player): Call<Player>

    @DELETE("/api/players/{id}")
    fun deletePlayer(@Path("id") id: Long): Call<Void>

    // teams :

    @GET("/api/teams")
    fun getTeams(): Call<List<Team>>

    @POST("/api/teams")
    fun addTeam(@Body team: Team): Call<Team>

    @PUT("/api/teams/{id}")
    fun updateTeam(@Path("id") id: Long, @Body team: Team): Call<Team>

    @DELETE("/api/teams/{id}")
    fun deleteTeam(@Path("id") id: Long): Call<Void>

    // matches :

    @GET("/api/matches")
    fun getMatches(): Call<List<Match>>

    @POST("/api/matches")
    fun addMatch(@Body match: Match): Call<Match>

    @PUT("/api/matches/{id}")
    fun updateMatch(@Path("id") id: Long, @Body match: Match): Call<Match>

    @DELETE("/api/matches/{id}")
    fun deleteMatch(@Path("id") id: Long): Call<Void>

    // matche result :

    @GET("/api/match-results")
    fun getMatchResults(): Call<List<MatchResult>>

    @POST("/api/match-results")
    fun addMatchResult(@Body result: MatchResult): Call<MatchResult>

    @PUT("/api/match-results/{id}")
    fun updateMatchResult(@Path("id") id: Long, @Body result: MatchResult): Call<MatchResult>

    @DELETE("/api/match-results/{id}")
    fun deleteMatchResult(@Path("id") id: Long): Call<Void>




}
