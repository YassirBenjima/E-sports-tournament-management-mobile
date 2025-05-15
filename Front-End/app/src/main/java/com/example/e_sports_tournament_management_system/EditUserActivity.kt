package com.example.e_sports_tournament_management_system

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.User
import com.example.e_sports_tournament_management_system.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class EditUserActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextFullName = findViewById<EditText>(R.id.editTextFullName)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        // Récupérer les données depuis l’intent
        userId = intent.getLongExtra("USER_ID", -1)
        val username = intent.getStringExtra("USERNAME") ?: ""
        val fullName = intent.getStringExtra("FULLNAME") ?: ""
        val email = intent.getStringExtra("EMAIL") ?: ""
        val password = intent.getStringExtra("PASSWORD") ?: ""

        // Pré-remplir les champs
        editTextUsername.setText(username)
        editTextFullName.setText(fullName)
        editTextEmail.setText(email)
        editTextPassword.setText(password)

        // Changer le texte du bouton
        btnSubmit.text = "Mettre à jour"

        // Retrofit init
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080") // Remplace si besoin
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Soumettre mise à jour
        btnSubmit.setOnClickListener {
            val updatedUser = User(
                id = userId,
                username = editTextUsername.text.toString(),
                fullName = editTextFullName.text.toString(),
                email = editTextEmail.text.toString(),
                password = editTextPassword.text.toString()
            )

            apiService.updateUser(userId, updatedUser).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditUserActivity, "Utilisateur mis à jour", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@EditUserActivity, "Erreur ${response.code()}: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@EditUserActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Retour
        btnBack.setOnClickListener {
            finish()
        }
    }
}
