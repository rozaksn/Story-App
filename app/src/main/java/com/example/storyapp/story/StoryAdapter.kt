package com.example.storyapp.story

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ItemStoryBinding
import com.example.storyapp.detail.DetailActivity
import com.example.storyapp.response.ListStoryItem

class StoryAdapter :RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    private val story = ArrayList<ListStoryItem>()

      class StoryViewHolder(private val binding: ItemStoryBinding):RecyclerView.ViewHolder(binding.root) {
          fun bind(story: ListStoryItem) {
              binding.apply {
                  Glide.with(itemView.context).load(story.photoUrl).into(ivStory)
                  tvUsername.text = story.name
                  tvDesc.text = story.description

                  itemView.setOnClickListener {
                      val intent = Intent(itemView.context, DetailActivity::class.java)
                      intent.putExtra(EXTRA_NAME, story.name)
                      intent.putExtra(EXTRA_DESCRIPTION, story.description)
                      intent.putExtra(EXTRA_IMAGE, story.photoUrl)


                      val activityOptionCompat: ActivityOptionsCompat =
                          ActivityOptionsCompat.makeSceneTransitionAnimation(
                              itemView.context as Activity,
                              Pair(ivStory, "photo"),
                              Pair(tvUsername, "name"),
                              Pair(tvDesc, "description")
                          )
                      itemView.context.startActivity(intent, activityOptionCompat.toBundle())
                  }
              }
          }
      }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return StoryViewHolder(view)
    }

    override fun getItemCount(): Int = story.size

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(story[position])
    }

    @SuppressLint("NotifyDataChanged")
    fun setListStory(listStory:ArrayList<ListStoryItem>){
        story.clear()
        story.addAll(listStory)
        notifyDataSetChanged()
    }

    companion object{
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_IMAGE = "extra_image"
    }

}