package com.submission.storyapps.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileUtils {
    fun uriToFile(imageUri: Uri, context: Context): File {
        val tempFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(tempFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
        outputStream.close()
        inputStream.close()
        return tempFile
    }
    private fun createCustomTempFile(context: Context): File {
        return File.createTempFile("temp_", ".jpg", context.cacheDir)
    }
}