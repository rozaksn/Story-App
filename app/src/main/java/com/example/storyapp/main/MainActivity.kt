package com.example.storyapp.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.adapter.LoadingAdapter
import com.example.storyapp.add.AddStoryActivity
import com.example.storyapp.data.DataStoreViewModels
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.entity.Story
import com.example.storyapp.login.LoginActivity
import com.example.storyapp.maps.MapsActivity
import com.example.storyapp.story.StoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalPagingApi
class MainActivity : AppCompatActivity() {
    private  val binding by lazy(LazyThreadSafetyMode.NONE){
     ActivityMainBinding.inflate(layoutInflater)}
    private val mainViewModel by viewModels<MainViewModel>()
    private val dataStoreViewModel by viewModels<DataStoreViewModels>()
    private var token:String=""
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.app_name)

        storyAdapter = StoryAdapter()
        getStory()
        setupViewModel()



        getToken()


        addStory()


    }




    private fun getToken(){
        lifecycleScope.launchWhenCreated {
            launch {
                mainViewModel.getToken().collect{ myToken->
                    if (!myToken.isNullOrEmpty()) token=myToken

                }
            }
        }
    }

    private fun update(story: PagingData<Story>){
        val state = binding.rvStories.layoutManager?.onSaveInstanceState()
        storyAdapter.submitData(lifecycle,story)
        binding.rvStories.layoutManager?.onRestoreInstanceState(state)

    }

    private fun getStory(){
        mainViewModel.getListStories(token).observe(this){storyResult->
            update(storyResult)
        }
    }

    private fun setRecyclerView() {
        binding.apply {
            rvStories.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStories.setHasFixedSize(true)
            rvStories.adapter = storyAdapter.withLoadStateFooter(footer = LoadingAdapter{storyAdapter.retry()})

        }
    }

    private fun addStory(){
        binding.fbAddStory.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity).toBundle())
        }
        binding.fbRefresh.setOnClickListener {
            this.setupViewModel()

        }
    }

     private fun setupViewModel() {
         dataStoreViewModel.getUserModel().observe(this@MainActivity) { userModel ->
             if (!userModel.isLogin) {
                 Log.d(TAG,token)
                 val intent = Intent(this, LoginActivity::class.java)
                 startActivity(intent/*,ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity).toBundle()*/)
                 finish()
             } else {
                 mainViewModel.getListStories(token).observe(this) {result->
                     setRecyclerView()
                     update(result)

                     showLoading(false)
                 }


             }


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
            R.id.maps_menu -> {
                val intentMap = Intent(this@MainActivity,MapsActivity::class.java)
                startActivity(intentMap)
                finish()
                true
            }
            R.id.logout_action -> {
                dataStoreViewModel.userLogout()
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
