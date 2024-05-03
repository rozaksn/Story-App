package com.example.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.example.storyapp.databinding.ItemLoadingBinding

class LoadingAdapter(private val retry:() -> Unit): LoadStateAdapter<LoadingAdapterViewHolder>(){
    override fun onBindViewHolder(holder: LoadingAdapterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingAdapterViewHolder {
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LoadingAdapterViewHolder(binding,retry)
    }

}