package com.example.e_sports_tournament_management_system

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.Team
import com.example.e_sports_tournament_management_system.network.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class EditTeamActivity : AppCompatActivity() {

    private var teamId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_team)

        val nameField = findViewById<EditText>(R.id.editTextTeamName)
        val countryField = findViewById<EditText>(R.id.editTextTeamCountry)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        teamId = intent.getLongExtra("TEAM_ID", -1)
        nameField.setText(intent.getStringExtra("TEAM_NAME"))
        countryField.setText(intent.getStringExtra("TEAM_COUNTRY"))

        btnSubmit.text = "Mettre à jour"

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        btnSubmit.setOnClickListener {
            val name = nameField.text.toString().trim()
            val country = countryField.text.toString().trim()

            if (name.isEmpty() || country.isEmpty()) {
                Toast.makeText(this, "Champs requis", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedTeam = Team(id = teamId, name = name, country = country, logoUrl = null)

            apiService.updateTeam(teamId, updatedTeam).enqueue(object : Callback<Team> {
                override fun onResponse(call: Call<Team>, response: Response<Team>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditTeamActivity, "Équipe mise à jour", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@EditTeamActivity, "Erreur: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Team>, t: Throwable) {
                    Toast.makeText(this@EditTeamActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        btnBack.setOnClickListener { finish() }
    }
}
