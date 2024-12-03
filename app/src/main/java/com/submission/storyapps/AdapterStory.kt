package com.submission.storyapps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.storyapps.databinding.StoryListBinding
import com.submission.storyapps.model.Story

class AdapterStory(private val stories: List<Story>) : RecyclerView.Adapter<AdapterStory.StoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }
    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int = stories.size

    inner class StoryViewHolder(private val binding: StoryListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.name.text = story.name
            binding.desc.text = story.description
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.ivPict)
        }
    }
}