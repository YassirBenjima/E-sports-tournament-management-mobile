package com.example.e_sports_tournament_management_system

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
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

class EditGameActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private var currentImageUrl: String? = null
    private var gameId: Long = -1L

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_game)

        val editName = findViewById<EditText>(R.id.editTextGameName)
        val editPlatform = findViewById<EditText>(R.id.editTextPlatform)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)
        val btnSelectImage = findViewById<Button>(R.id.btnSelectImage)
        val imagePreview = findViewById<ImageView>(R.id.imagePreview)

        // Récupérer les données envoyées dans l'Intent
        gameId = intent.getLongExtra("GAME_ID", -1L)
        val gameName = intent.getStringExtra("GAME_NAME") ?: ""
        val gamePlatform = intent.getStringExtra("GAME_PLATFORM") ?: ""
        currentImageUrl = intent.getStringExtra("GAME_IMAGE_URL")

        editName.setText(gameName)
        editPlatform.setText(gamePlatform)

        // Afficher l'image existante si présente
        if (!currentImageUrl.isNullOrEmpty()) {
            imagePreview.visibility = ImageView.VISIBLE
            Glide.with(this)
                .load(currentImageUrl)
                .into(imagePreview)
        } else {
            imagePreview.visibility = ImageView.GONE
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        btnSubmit.text = "Mettre à jour"

        btnBack.setOnClickListener { finish() }

        btnSelectImage.setOnClickListener {
            openImageChooser()
        }

        btnSubmit.setOnClickListener {
            val name = editName.text.toString().trim()
            val platform = editPlatform.text.toString().trim()

            if (name.isEmpty() || platform.isEmpty()) {
                Toast.makeText(this, "Champs requis", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Si une nouvelle image a été sélectionnée, upload puis update avec nouvelle URL
            if (selectedImageUri != null) {
                uploadImageAndUpdateGame(name, platform)
            } else {
                // Sinon on update juste avec l'URL existante
                updateGame(name, platform, currentImageUrl ?: "")
            }
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

    private fun uploadImageAndUpdateGame(name: String, platform: String) {
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
                    val imageUrl = response.body()!!.string()
                    updateGame(name, platform, imageUrl)
                } else {
                    Toast.makeText(this@EditGameActivity, "Échec de l'upload de l'image", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@EditGameActivity, "Erreur upload image: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateGame(name: String, platform: String, imageUrl: String) {
        val updatedGame = Game(id = gameId, name = name, platform = platform, imageUrl = imageUrl)
        apiService.updateGame(gameId, updatedGame).enqueue(object : Callback<Game> {
            override fun onResponse(call: Call<Game>, response: Response<Game>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditGameActivity, "Jeu mis à jour", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@EditGameActivity, "Erreur ${response.code()} : $errorBody", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Game>, t: Throwable) {
                Toast.makeText(this@EditGameActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
