package com.example.storyapp.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.story.StoryAdapter
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.login.LoginActivity
import com.example.storyapp.story.AddStoryActivity
import com.example.storyapp.user.UserPreference


class MainActivity : AppCompatActivity() {
    private  val binding by lazy(LazyThreadSafetyMode.NONE){
     ActivityMainBinding.inflate(layoutInflater)}

    private lateinit var mainViewModel: MainViewModel
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.app_name)

        userPreference = UserPreference(this)
        storyAdapter = StoryAdapter()

        setupViewModel()
        userValidation()
        addStory()
        setRecyclerView()



    }

    override fun onResume() {
        super.onResume()
        this.setupViewModel()
    }

    private fun userValidation(){
        if (!userPreference.getUser().isLogin){
            val userLogin = userPreference.getUser().isLogin
            Log.d(TAG,userLogin.toString())
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity).toBundle())
            finish()
        }
    }



    private fun setRecyclerView() {
        binding.apply {
            rvStories.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStories.setHasFixedSize(true)
            rvStories.adapter = storyAdapter
        }
    }

    private fun addStory(){
        binding.fbAddStory.setOnClickListener {
            val intent = Intent(this@MainActivity,AddStoryActivity::class.java)
            startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity).toBundle())
        }
        binding.fbRefresh.setOnClickListener {
            this.setupViewModel()
        }
    }

     private fun setupViewModel() {
        mainViewModel = ViewModelProvider(this) [MainViewModel::class.java]
        mainViewModel.getList(userPreference.getUser().token)
        mainViewModel.listStories.observe(this){
            if ( it != null){
                storyAdapter.setListStory(it)
            }
        }
        mainViewModel.isLoading.observe(this){
            showLoading(it)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.translate_action -> {
               val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                   startActivity(intent)
                finish()
                true

            }
            R.id.logout_action -> {
                userPreference.logout()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
                true
            }else -> {return super.onOptionsItemSelected(item)}
        }

    }
    private fun showLoading(isLoading: Boolean) {
        binding.pbStory.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAG = "extra_tag"
    }

}