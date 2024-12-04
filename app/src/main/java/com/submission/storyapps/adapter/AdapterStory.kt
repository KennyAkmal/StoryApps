package com.submission.storyapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.storyapps.databinding.StoryListBinding
import com.submission.storyapps.model.Story

class AdapterStory(
    private val stories: List<Story>,
    private val onItemClicked: (Story, View) -> Unit
) : RecyclerView.Adapter<AdapterStory.StoryViewHolder>() {

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
            binding.tvItemName.text = story.name
            binding.tvItemDescription.text = story.description
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)
            binding.ivItemPhoto.transitionName = "story_image_${story.id}"
            binding.root.setOnClickListener {
                onItemClicked(story, binding.ivItemPhoto)
            }
        }
    }
}
