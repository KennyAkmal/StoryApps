package com.submission.storyapps.ui.detail

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.submission.storyapps.R
import com.submission.storyapps.databinding.ActivityDetailBinding
import com.submission.storyapps.model.Story
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<Story>(EXTRA_STORY)
        story?.let { populateDetail(it) }
        animateDetail()
    }

    private fun populateDetail(story: Story) {
        binding.tvDetailName.text = story.name
        binding.tvDetailDescription.text = story.description

        Glide.with(this)
            .load(story.photoUrl)
            .placeholder(R.drawable.choosepicture)
            .into(binding.ivDetailPhoto)
    }

    private fun animateDetail() {
        val fadeIn = ObjectAnimator.ofFloat(binding.root, "alpha", 0f, 1f)
        fadeIn.duration = 500
        fadeIn.start()
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}

