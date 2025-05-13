package com.example.e_sports_tournament_management_system

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.User
import com.example.e_sports_tournament_management_system.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user) // Assurez-vous que le layout XML est bien défini

        // Récupérer les références des champs EditText
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextFullName = findViewById<EditText>(R.id.editTextFullName)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        // Configuration de Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080") // Remplace par l'URL de ton API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Gérer le clic sur le bouton Ajouter
        btnSubmit.setOnClickListener {
            val username = editTextUsername.text.toString()
            val fullName = editTextFullName.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (username.isNotEmpty() && fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val newUser = User(
                    id = 0, // L'id sera généré par le backend
                    username = username,
                    password = password,
                    fullName = fullName,
                    email = email
                )

                // Envoi de la requête POST pour ajouter un utilisateur
                apiService.addUser(newUser).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        Log.d("AddUserActivity", "Code de réponse : ${response.code()}")
                        if (response.isSuccessful) {
                            Log.d("AddUserActivity", "Utilisateur ajouté : ${response.body()}")
                            Toast.makeText(this@AddUserActivity, "Utilisateur ajouté avec succès!", Toast.LENGTH_SHORT).show()
                        } else {
                            // Log des erreurs côté serveur
                            Log.e("AddUserActivity", "Erreur de serveur : ${response.errorBody()?.string()}")
                            Toast.makeText(this@AddUserActivity, "Erreur lors de l'ajout de l'utilisateur", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        // Log l'erreur réseau
                        Log.e("AddUserActivity", "Erreur réseau: ${t.message}")
                        Toast.makeText(this@AddUserActivity, "Erreur réseau: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }

        // Bouton retour vers le Dashboard
        btnBack.setOnClickListener {
            finish() // Fermer cette activité pour revenir en arrière
        }
    }
}
