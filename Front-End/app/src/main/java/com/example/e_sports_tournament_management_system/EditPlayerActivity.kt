    package com.example.e_sports_tournament_management_system

    import android.app.Activity
    import android.content.Intent
    import android.net.Uri
    import android.os.Bundle
    import android.widget.*
    import androidx.appcompat.app.AppCompatActivity
    import com.bumptech.glide.Glide
    import com.example.e_sports_tournament_management_system.model.Player
    import com.example.e_sports_tournament_management_system.network.ApiService
    import com.example.e_sports_tournament_management_system.utils.FileUtils
    import okhttp3.MediaType.Companion.toMediaTypeOrNull
    import okhttp3.MultipartBody
    import okhttp3.RequestBody.Companion.asRequestBody
    import okhttp3.ResponseBody
    import retrofit2.*
    import retrofit2.converter.gson.GsonConverterFactory
    import java.io.File

    class EditPlayerActivity : AppCompatActivity() {

        private val PICK_IMAGE_REQUEST = 1
        private var selectedImageUri: Uri? = null
        private var currentImageUrl: String? = null
        private var playerId: Long = -1L

        private lateinit var apiService: ApiService

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_edit_player)

            val usernameField = findViewById<EditText>(R.id.editTextPlayerName)
            val ageField = findViewById<EditText>(R.id.editTextPlayerAge)
            val nationalityField = findViewById<EditText>(R.id.editTextPlayerNationality)
            val btnSubmit = findViewById<Button>(R.id.btnSubmit)
            val btnBack = findViewById<ImageButton>(R.id.btnBackDashboard)
            val btnSelectImage = findViewById<Button>(R.id.btnSelectImage)
            val imagePreview = findViewById<ImageView>(R.id.imagePreview)

            playerId = intent.getLongExtra("PLAYER_ID", -1L)
            usernameField.setText(intent.getStringExtra("PLAYER_NAME"))
            ageField.setText(intent.getStringExtra("PLAYER_AGE"))
            nationalityField.setText(intent.getStringExtra("PLAYER_NATIONALITY"))
            currentImageUrl = intent.getStringExtra("PLAYER_IMAGE_URL")

            // Affiche l'image existante si présente
            if (!currentImageUrl.isNullOrEmpty()) {
                imagePreview.visibility = ImageView.VISIBLE
                Glide.with(this).load(currentImageUrl).into(imagePreview)
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
                val username = usernameField.text.toString().trim()
                val ageText = ageField.text.toString().trim()
                val nationality = nationalityField.text.toString().trim()

                if (username.isEmpty() || ageText.isEmpty() || nationality.isEmpty()) {
                    Toast.makeText(this, "Champs requis", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val age = ageText.toIntOrNull()
                if (age == null) {
                    Toast.makeText(this, "L'âge doit être un nombre", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Si une nouvelle image a été sélectionnée, upload puis update avec nouvelle URL
                if (selectedImageUri != null) {
                    uploadImageAndUpdatePlayer(username, age, nationality)
                } else {
                    // Sinon update juste avec l'URL existante
                    updatePlayer(username, age, nationality, currentImageUrl ?: "")
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

        private fun uploadImageAndUpdatePlayer(username: String, age: Int, nationality: String) {
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
                        updatePlayer(username, age, nationality, imageUrl)
                    } else {
                        Toast.makeText(this@EditPlayerActivity, "Échec de l'upload de l'image", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@EditPlayerActivity, "Erreur upload image: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        private fun updatePlayer(username: String, age: Int, nationality: String, imageUrl: String) {
            val updatedPlayer = Player(
                id = playerId,
                username = username,
                age = age,
                nationality = nationality,
                imageUrl = imageUrl
            )
            apiService.updatePlayer(playerId, updatedPlayer).enqueue(object : Callback<Player> {
                override fun onResponse(call: Call<Player>, response: Response<Player>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditPlayerActivity, "Joueur mis à jour", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(
                            this@EditPlayerActivity,
                            "Erreur ${response.code()} : $errorBody",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Player>, t: Throwable) {
                    Toast.makeText(this@EditPlayerActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
