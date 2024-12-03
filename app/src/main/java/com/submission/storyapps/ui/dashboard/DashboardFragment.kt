package com.submission.storyapps.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.storyapps.AdapterStory
import com.submission.storyapps.AddStoryActivity
import com.submission.storyapps.LoginActivity
import com.submission.storyapps.databinding.FragmentDashboardBinding
import com.submission.storyapps.model.Story
import com.submission.storyapps.model.StoryResponse
import com.submission.storyapps.network.ApiClient
import com.submission.storyapps.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        fetchStories()
        return binding.root
    }

    private fun fetchStories() {
        val apiService = ApiClient.create(requireContext())
        apiService.getAllStories(page = 1, size = 10).enqueue(object : Callback<StoryResponse> {
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
        binding.rvStory.adapter = AdapterStory(stories)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
