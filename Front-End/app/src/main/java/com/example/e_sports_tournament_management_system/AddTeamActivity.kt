package com.example.e_sports_tournament_management_system

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.Team
import com.example.e_sports_tournament_management_system.network.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class AddTeamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_team)

        val nameField = findViewById<EditText>(R.id.editTextTeamName)
        val countryField = findViewById<EditText>(R.id.editTextTeamCountry)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        btnBack.setOnClickListener { finish() }

        btnSubmit.setOnClickListener {
            val name = nameField.text.toString().trim()
            val country = countryField.text.toString().trim()

            if (name.isEmpty() || country.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newTeam = Team(name = name, country = country, logoUrl = null)

            apiService.addTeam(newTeam).enqueue(object : Callback<Team> {
                override fun onResponse(call: Call<Team>, response: Response<Team>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddTeamActivity, "Équipe ajoutée", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddTeamActivity, "Erreur: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Team>, t: Throwable) {
                    Toast.makeText(this@AddTeamActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
