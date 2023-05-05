package com.example.storyapp

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MM-yyyy"
val timeStamp:String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())

 fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp,"jpg",storageDir)
}

 fun uriToFile(selectedImage: Uri, context: Context): File {
    val contentResolve: ContentResolver = context.contentResolver
    val createFile = createTempFile(context)
    val inputStream = contentResolve.openInputStream(selectedImage) as InputStream
    val outputStream: OutputStream = FileOutputStream(createFile)
    val byteArray = ByteArray(1024)
    var len:Int
    while (inputStream.read(byteArray).also { len=it }>0)outputStream.write(byteArray,0,len)
    outputStream.close()
    inputStream.close()

    return createFile
}

 fun reduceFileSize(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressPict = 100
    var streamLength: Int
    do{
        val bitmapStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressPict,bitmapStream)
        val bmpByteArray = bitmapStream.toByteArray()
        streamLength = bmpByteArray.size
        compressPict -= 5
    }while (streamLength > 1000000)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressPict, FileOutputStream(file))
    return file
}