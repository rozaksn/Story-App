package com.example.storyapp.add

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.R
import com.example.storyapp.createTempFile
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.main.MainActivity
import com.example.storyapp.reduceFileSize
import com.example.storyapp.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
@ExperimentalPagingApi
class AddStoryActivity : AppCompatActivity() {
    private  val binding by lazy(LazyThreadSafetyMode.NONE){
        ActivityAddStoryBinding.inflate(layoutInflater)}
    private val addStoryViewModel by viewModels<AddStoryViewModel>()
    private lateinit var curentPhotoPath: String
    private var getFile:File? =null
    private var location:Location? = null
    private lateinit var fusedLocation :FusedLocationProviderClient
    private var token:String = ""




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
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        //userPref = UserPreference(this)



        getToken()
        buttonAction()
        btnUploadAction()

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

    private fun getToken(){
        lifecycleScope.launchWhenCreated {
            launch {
                addStoryViewModel.getToken().collect{ myToken->
                    if (!myToken.isNullOrEmpty()) token=myToken

                }
            }
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
        showLoading(true)
        lifecycleScope.launchWhenStarted {
            launch {
                if (getFile != null) {
                    val description = binding.edTextDesc
                    val descText = description.text.toString().toRequestBody("text/plain".toMediaType())
                    val reduceFile = reduceFileSize(getFile as File)
                    val reqImage = reduceFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part =
                        MultipartBody.Part.createFormData("photo", reduceFile.name, reqImage)

                    var lat: RequestBody? = null
                    var lon: RequestBody? = null
                    if (location != null) {
                        lat =
                            location?.latitude.toString().toRequestBody("text/plain".toMediaType())
                        lon =
                            location?.longitude.toString().toRequestBody("text/plain".toMediaType())
                    }

                        addStoryViewModel.uploadStory(token, imageMultipart, descText, lat, lon).collect { response->
                        response.onSuccess {
                            showLoading(true)
                            Toast.makeText(this@AddStoryActivity,getString(R.string.success_upload),Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        response.onFailure {
                            showLoading(true)
                            Toast.makeText(this@AddStoryActivity,getString(R.string.failed_upload),Toast.LENGTH_SHORT).show()

                        }

                        }
                }
            }
        }



    }

    private val requestPermissionLocation = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()
    ){locPermission->
        Log.d(TAG,"$locPermission")
        when{
            locPermission[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getLocationUser()
            }else->{
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also { intent->
                    val uriParts= Uri.fromParts("package",packageName,null)
                    intent.data = uriParts
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        }
    }

    private fun getLocationUser(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            fusedLocation.lastLocation.addOnSuccessListener { location ->
                if (location != null){
                    this.location = location
                    Log.d(TAG,"latitude: ${location.latitude}, longitude: ${location.longitude}")
                }
            }
        }else{
            requestPermissionLocation.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))

        }
    }

    private fun btnUploadAction(){
        val edDesc = binding.edTextDesc
        val button = binding.btUpload
        button.isEnabled = false
        edDesc.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val desc = s.toString().trim()
                button.isEnabled = desc.length >= 1
            }

        })

    }

    private fun buttonAction(){
        binding.btCamera.setOnClickListener { takePhoto() }
        binding.btGallery.setOnClickListener { runIntentGallery() }
        binding.btUpload.setOnClickListener {
            uploadStory()
            val intent = Intent(this@AddStoryActivity,MainActivity::class.java)
            startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation(this@AddStoryActivity as Activity).toBundle())
            //finish()


        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.pbAdd.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object{
        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
        const val TAG = "extra_tag_map"
    }
}