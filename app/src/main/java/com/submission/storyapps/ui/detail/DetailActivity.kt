package com.submission.storyapps.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.submission.storyapps.databinding.ActivityDetailBinding
import com.submission.storyapps.model.Story
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val story = intent.getParcelableExtra<Story>(EXTRA_STORY)
        story?.let { populateDetail(it) }

        val imageView = binding.ivDetailPhoto
        imageView.transitionName = "story_image_${story?.id}"
    }
    private fun populateDetail(story: Story) {
        binding.tvDetailName.text = story.name
        binding.tvDetailDescription.text = story.description
        Glide.with(this)
            .load(story.photoUrl)
            .into(binding.ivDetailPhoto)
    }
    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}
