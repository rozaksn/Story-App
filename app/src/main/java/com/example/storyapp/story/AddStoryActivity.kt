package com.example.storyapp.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.ContentValues.*
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.storyapp.API.ApiConfig
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.main.MainActivity
import com.example.storyapp.reduceFileSize
import com.example.storyapp.response.UploadResponse
import com.example.storyapp.uriToFile
import com.example.storyapp.user.UserPreference
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import com.example.storyapp.createTempFile
import retrofit2.*

class AddStoryActivity : AppCompatActivity() {
    private  val binding by lazy(LazyThreadSafetyMode.NONE){
        ActivityAddStoryBinding.inflate(layoutInflater)}

    private lateinit var userPref:UserPreference
    private lateinit var curentPhotoPath: String
    private var getFile:File? =null

    companion object{
        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION){
            if (!permissionGranted()){
                Toast.makeText(this@AddStoryActivity,getString(R.string.permission_not_accepted),Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun permissionGranted() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it)==PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.add_story)

        userPref = UserPreference(this)

        binding.btCamera.setOnClickListener { takePhoto() }
        binding.btGallery.setOnClickListener { runIntentGallery() }
        binding.btUpload.setOnClickListener { uploadStory()
            uploadStory()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }



    private fun takePhoto(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoUri :Uri = FileProvider.getUriForFile(this@AddStoryActivity,
                "com.example.storyapp",it)
            curentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
            launchIntentCamera.launch(intent)
        }
    }

    private val launchIntentCamera = registerForActivityResult(ActivityResultContracts
        .StartActivityForResult()) {
        if (it.resultCode == RESULT_OK){
            val mfile = File(curentPhotoPath)
            getFile = mfile

            val imgResult=BitmapFactory.decodeFile(getFile?.path)
            binding.ivDesc.setImageBitmap(imgResult)
        }

    }

    private fun runIntentGallery(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent,getString(R.string.choose_picture))
        launchIntentGallery.launch(chooser)
    }

    private val launchIntentGallery = registerForActivityResult(ActivityResultContracts
        .StartActivityForResult()){result ->
        if (result.resultCode == RESULT_OK){
            val selectedImage:Uri = result.data?.data as Uri
            val uriFile = uriToFile(selectedImage,this@AddStoryActivity)
            getFile = uriFile
            binding.ivDesc.setImageURI(selectedImage)
        }
    }

    private fun uploadStory(){
        if (getFile != null){
            val reduceFile= reduceFileSize(getFile as File)
            val  descText = binding.edTextDesc.text.toString()
            val desc = descText.toRequestBody("text/plain".toMediaType())
            val reqImageFile = reduceFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imgMultipart:MultipartBody.Part = MultipartBody.Part.createFormData("photo",reduceFile.name,reqImageFile)

            val token = "Bearer ${userPref.getUser().token}"
            val client = ApiConfig().getApiService().uploadStories(token,imgMultipart,desc)
            client.enqueue(object : Callback<UploadResponse> {
                override fun onResponse(
                    call: Call<UploadResponse>,
                    response: Response<UploadResponse>
                ) {
                    val responseBody = response.body()
                    Log.d(TAG,"onResponse: ${responseBody}")
                    if (response.isSuccessful && responseBody?.message == getString(R.string.success_upload) ){
                        Toast.makeText(this@AddStoryActivity,getString(R.string.success_upload),Toast.LENGTH_SHORT).show()
                    }
                }


                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    showLoading(false)
                    Log.e(TAG,"onFailure: ${t.message}")
                    Toast.makeText(this@AddStoryActivity,getString(R.string.failed_upload),Toast.LENGTH_SHORT).show()
                }


            })
        }
        else showLoading(false)

    }


    private fun showLoading(isLoading: Boolean) {
        binding.pbAdd.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}