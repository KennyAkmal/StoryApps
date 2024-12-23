package com.submission.storyapps.ui.story

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.submission.storyapps.databinding.ActivityAddStoryBinding
import com.submission.storyapps.model.AddStoryResponse
import com.submission.storyapps.network.RetrofitClient
import com.submission.storyapps.utils.SessionManager
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var selectedImageUri: Uri? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: Float? = null
    private var lon: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = SessionManager(this).getToken() ?: ""
        if (token.isEmpty()) {
            Toast.makeText(this, "Token tidak ditemukan, silakan login ulang", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        checkPermissions()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        binding.btnSelectImage.setOnClickListener {
            selectImage()
        }

        binding.btnAdd.setOnClickListener {
            val description = binding.edAddDescription.text.toString().trim()
            if (description.isNotEmpty() && selectedImageUri != null) {
                val file = compressImage(getFileFromUri(selectedImageUri!!))
                uploadStory(token, description, file, lat, lon)
            } else {
                Toast.makeText(this, "Deskripsi atau gambar belum dipilih", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    lat = it.latitude.toFloat()
                    lon = it.longitude.toFloat()
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
        }
    }

    private fun uploadStory(token: String, description: String, file: File, lat: Float?, lon: Float?) {
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())
        val imagePart = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        )
        val latBody = lat?.toString()?.toRequestBody("text/plain".toMediaType())
        val lonBody = lon?.toString()?.toRequestBody("text/plain".toMediaType())

        val apiService = RetrofitClient.getApiService()
        apiService.addNewStory(
            token = "Bearer $token",
            description = descriptionBody,
            photo = imagePart,
            lat = latBody,
            lon = lonBody
        ).enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(call: Call<AddStoryResponse>, response: Response<AddStoryResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddStoryActivity, "Story uploaded successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddStoryActivity, "Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                Toast.makeText(this@AddStoryActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            binding.ivPreviewImage.setImageURI(selectedImageUri)
        }
    }

    private fun getFileFromUri(uri: Uri): File {
        val contentResolver: ContentResolver = this.contentResolver
        val fileName = contentResolver.getFileName(uri) ?: "temp_file.jpg"

        val file = File(cacheDir, fileName)
        val inputStream = contentResolver.openInputStream(uri) ?: throw IllegalArgumentException("Unable to open InputStream for URI")
        val outputStream = FileOutputStream(file)

        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return file
    }

    private fun ContentResolver.getFileName(uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex != -1) {
                        return it.getString(columnIndex)
                    }
                }
            }
        }
        return uri.lastPathSegment
    }

    private fun compressImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var quality = 100
        val compressedFile = File(cacheDir, "compressed_${file.name}")

        do {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            val byteArray = outputStream.toByteArray()

            if (byteArray.size <= MAX_FILE_SIZE) {
                compressedFile.outputStream().use {
                    it.write(byteArray)
                }
                break
            }

            quality -= 10
        } while (quality > 10)
        return compressedFile
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
        }
    }

    companion object {
        private const val REQUEST_SELECT_IMAGE = 1
        private const val PERMISSION_REQUEST_CODE = 100
        private const val MAX_FILE_SIZE = 1_000_000
    }
}
