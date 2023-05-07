package com.example.storyapp.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE){
        ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        detail()

    }

    private fun detail(){
        val img = intent.getStringExtra(EXTRA_IMAGE)
        val name = intent.getStringExtra(EXTRA_NAME)
        val description = intent.getStringExtra(EXTRA_DESC)
        binding.apply {
            Glide.with(this@DetailActivity).load(img).into(ivStoryDetail)
            tvUsernameDetail.text = name
            tvDescDetail.text = description
        }
    }

    companion object{
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESC = "extra_description"
    }
}