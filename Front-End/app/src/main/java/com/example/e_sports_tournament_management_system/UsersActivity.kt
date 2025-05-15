package com.example.e_sports_tournament_management_system

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_sports_tournament_management_system.adapter.UserAdapter
import com.example.e_sports_tournament_management_system.model.User
import com.example.e_sports_tournament_management_system.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class UsersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        recyclerView = findViewById(R.id.recyclerUsers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Retrofit setup
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Bouton retour vers le Dashboard
        val btnBack = findViewById<android.widget.ImageButton>(R.id.btnBackDashboard)
        btnBack.setOnClickListener {
            finish() // Fermer cette activité pour revenir en arrière (DashboardActivity sera affichée)
        }

        // Bouton Ajouter Utilisateur
        val btnAddUser = findViewById<Button>(R.id.btnAddUser)
        btnAddUser.setOnClickListener {
            val intent = Intent(this@UsersActivity, AddUserActivity::class.java)
            startActivity(intent) // Rediriger vers l'activité AddUserActivity
        }

        fetchUsers()
    }

    private fun fetchUsers() {
        apiService.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()
                    recyclerView.adapter = UserAdapter(users,
                        onEdit = { user ->
                            val intent = Intent(this@UsersActivity, EditUserActivity::class.java)
                            intent.putExtra("USER_ID", user.id)
                            intent.putExtra("USERNAME", user.username)
                            intent.putExtra("FULLNAME", user.fullName)
                            intent.putExtra("EMAIL", user.email)
                            intent.putExtra("PASSWORD", user.password)
                            startActivity(intent)
                        },
                        onDelete = { user ->
                            apiService.deleteUser(user.id!!).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    Toast.makeText(this@UsersActivity, "Supprimé", Toast.LENGTH_SHORT).show()
                                    fetchUsers()
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Toast.makeText(this@UsersActivity, "Erreur suppression", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    )
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@UsersActivity, "Échec : ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }
    override fun onResume() {
        super.onResume()
        fetchUsers() // Recharge la liste à chaque retour sur cette activité
    }


}
