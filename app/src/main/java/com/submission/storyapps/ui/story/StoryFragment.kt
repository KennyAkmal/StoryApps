package com.submission.storyapps.ui.story

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.storyapps.adapter.PagingAdapter
import com.submission.storyapps.databinding.FragmentStoryBinding
import com.submission.storyapps.model.Story
import com.submission.storyapps.network.ApiClient
import com.submission.storyapps.pagging.RepoImpl
import com.submission.storyapps.ui.detail.DetailActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StoryFragment : Fragment() {
    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PagingAdapter
    private lateinit var viewModel: ViewModelStory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        val apiService = ApiClient.create(requireContext())
        val repository = RepoImpl(apiService)
        val factory = ViewModelStoryFactory(repository)

        viewModel = ViewModelProvider(this, factory)[ViewModelStory::class.java]
        setupRecyclerView()
        observeData()

        return binding.root
    }

    private fun resetRecyclerViewAnimation() {
        binding.rvStory.alpha = 1f
        binding.rvStory.scaleX = 1f
        binding.rvStory.scaleY = 1f
    }

    override fun onResume() {
        super.onResume()
        resetRecyclerViewAnimation()
    }

    private fun setupRecyclerView() {
        adapter = PagingAdapter { story ->
            navigateToDetail(story)
        }
        binding.rvStory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStory.adapter = adapter
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.getStories().collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
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
