package com.submission.storyapps.ui.story

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import com.submission.storyapps.databinding.ActivityAddStoryBinding
import com.submission.storyapps.model.AddStoryResponse
import com.submission.storyapps.network.ApiClient
import com.submission.storyapps.utils.FileUtils
import com.submission.storyapps.utils.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var sessionManager: SessionManager
    private var selectedImageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.btnSelectImage.setOnClickListener {
            openGallery()
        }

        binding.buttonAdd.setOnClickListener {
            if (selectedImageFile != null && binding.edAddDescription.text.toString().isNotEmpty()) {
                uploadStory()
            } else {
                Toast.makeText(this, "Gambar dan deskripsi harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageFile = FileUtils.uriToFile(uri, this)
                binding.ivPreviewImage.setImageBitmap(BitmapFactory.decodeFile(selectedImageFile?.path))
            }
        }
    }

    private fun getFileFromUri(uri: Uri): File {
        val contentResolver: ContentResolver = contentResolver
        val tempFile = File(cacheDir, getFileName(uri))

        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(tempFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return compressImage(tempFile)
    }

    private fun getFileName(uri: Uri): String {
        var name = "temp_file"
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    name = it.getString(nameIndex)
                }
            }
        }
        return name
    }

    private fun compressImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        val compressedFile = File(cacheDir, "compressed_${file.name}")
        compressedFile.outputStream().use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        }
        return compressedFile
    }

    private fun uploadStory() {
        val description = binding.edAddDescription.text.toString()
        val descriptionBody = RequestBody.create("text/plain".toMediaTypeOrNull(), description)
        val imageBody = selectedImageFile?.let { file ->
            RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        }
        val imagePart = imageBody?.let {
            MultipartBody.Part.createFormData("photo", selectedImageFile!!.name, it)
        }

        val apiService = ApiClient.create(this)
        apiService.addNewStory(descriptionBody, imagePart).enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(call: Call<AddStoryResponse>, response: Response<AddStoryResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddStoryActivity, "Story berhasil diunggah!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddStoryActivity, "Gagal mengunggah story", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                Toast.makeText(this@AddStoryActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private const val REQUEST_CODE_SELECT_IMAGE = 100
    }
}
