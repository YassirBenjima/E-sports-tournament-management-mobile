package com.example.e_sports_tournament_management_system

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_sports_tournament_management_system.model.Game
import com.example.e_sports_tournament_management_system.network.ApiService
import com.example.e_sports_tournament_management_system.utils.FileUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class AddGameActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_game)

        val editName = findViewById<EditText>(R.id.editTextGameName)
        val editPlatform = findViewById<EditText>(R.id.editTextPlatform)
        val btnSelectImage = findViewById<Button>(R.id.btnSelectImage)
        val imagePreview = findViewById<ImageView>(R.id.imagePreview)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        btnBack.setOnClickListener { finish() }

        btnSelectImage.setOnClickListener {
            openImageChooser()
        }

        btnSubmit.setOnClickListener {
            val name = editName.text.toString().trim()
            val platform = editPlatform.text.toString().trim()

            if (name.isEmpty() || platform.isEmpty()) {
                Toast.makeText(this, "Tous les champs sont requis", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedImageUri == null) {
                Toast.makeText(this, "Veuillez sélectionner une image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Upload image puis ajouter le jeu avec URL de l'image
            uploadImageAndAddGame(name, platform)
        }
    }

    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Sélectionner une image"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            val imagePreview = findViewById<ImageView>(R.id.imagePreview)
            imagePreview.setImageURI(selectedImageUri)
            imagePreview.visibility = ImageView.VISIBLE
        }
    }

    private fun uploadImageAndAddGame(name: String, platform: String) {
        val file = FileUtils.getFile(this, selectedImageUri!!)
        if (file == null) {
            Toast.makeText(this, "Erreur lors de la récupération du fichier", Toast.LENGTH_SHORT).show()
            return
        }

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        apiService.uploadImage(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful && response.body() != null) {
                    // Lire la chaîne brute retournée par le serveur
                    val imageUrl = response.body()!!.string()
                    addGame(name, platform, imageUrl)
                } else {
                    Toast.makeText(this@AddGameActivity, "Échec de l'upload de l'image", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@AddGameActivity, "Erreur upload image: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun addGame(name: String, platform: String, imageUrl: String) {
        val game = Game(name = name, platform = platform, imageUrl = imageUrl)
        apiService.addGame(game).enqueue(object : Callback<Game> {
            override fun onResponse(call: Call<Game>, response: Response<Game>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddGameActivity, "Jeu ajouté", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddGameActivity, "Erreur: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Game>, t: Throwable) {
                Toast.makeText(this@AddGameActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
