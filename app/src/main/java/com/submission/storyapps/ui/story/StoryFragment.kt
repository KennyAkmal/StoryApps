package com.submission.storyapps.ui.story

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.storyapps.adapter.AdapterStory
import com.submission.storyapps.ui.detail.DetailActivity
import com.submission.storyapps.databinding.FragmentStoryBinding
import com.submission.storyapps.model.Story
import com.submission.storyapps.model.StoryResponse
import com.submission.storyapps.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StoryFragment : Fragment() {

    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        fetchStories()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        resetRecyclerViewAnimation()
        fetchStories()
    }

    private fun resetRecyclerViewAnimation() {
        binding.rvStory.alpha = 1f
        binding.rvStory.scaleX = 1f
        binding.rvStory.scaleY = 1f
    }

    private fun fetchStories() {
        val apiService = ApiClient.create(requireContext())
        apiService.getAllStories().enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    val stories = response.body()?.listStory ?: emptyList()
                    setupRecyclerView(stories)
                } else {
                    Toast.makeText(requireContext(), "Gagal memuat cerita", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView(stories: List<Story>) {
        binding.rvStory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStory.adapter = AdapterStory(stories) { story ->
            navigateToDetail(story)
        }
    }

    private fun navigateToDetail(story: Story) {
        val animation = ObjectAnimator.ofFloat(binding.rvStory, "alpha", 1f, 0f)
        animation.duration = 300
        animation.start()

        animation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_STORY, story)
                }
                startActivity(intent)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

