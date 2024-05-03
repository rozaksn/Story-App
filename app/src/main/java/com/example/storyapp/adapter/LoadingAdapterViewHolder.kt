package com.example.storyapp.adapter

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.databinding.ItemLoadingBinding

class LoadingAdapterViewHolder(private val binding: ItemLoadingBinding, retry:()-> Unit):
    RecyclerView
    .ViewHolder(binding.root) {
    init {
        binding.btRetry.setOnClickListener { retry.invoke() }
    }
    fun bind(loading: LoadState){
        binding.btRetry.isVisible = loading is LoadState.Error
        binding.pbLoading.isVisible = loading is LoadState.Loading
    }
}