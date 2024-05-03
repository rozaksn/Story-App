package com.example.storyapp.Splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.R
import com.example.storyapp.main.MainActivity
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//@AndroidEntryPoint
@ExperimentalPagingApi
//@HiltAndroidApp
//@SuppressLint("Splash")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        lifecycleScope.launch(){
            delay(1000)
            startActivity(Intent(this@SplashActivity,MainActivity::class.java))
            finish()
        }
    }

    override fun onPause() {
        lifecycleScope.cancel()
        super.onPause()
    }
}

