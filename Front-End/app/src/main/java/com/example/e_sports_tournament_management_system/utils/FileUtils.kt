package com.example.e_sports_tournament_management_system.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileUtils {
    fun getFile(context: Context, uri: Uri): File? {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val fileName = queryName(context, uri)
                val tempFile = File(context.cacheDir, fileName)
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                return tempFile
            } ?: return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }


    private fun queryName(context: Context, uri: Uri): String {
        var name = "temp_file"
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
        returnCursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex("_display_name")
                if (index != -1) {
                    name = it.getString(index)
                }
            }
        }
        if (name == "temp_file") {
            uri.lastPathSegment?.let { lastSegment ->
                name = lastSegment
            }
        }
        return name
    }
}
