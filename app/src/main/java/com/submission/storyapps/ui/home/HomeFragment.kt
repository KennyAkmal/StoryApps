package com.submission.storyapps.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.submission.storyapps.StarterActivity
import com.submission.storyapps.ui.story.AddStoryActivity
import com.submission.storyapps.databinding.FragmentHomeBinding
import com.submission.storyapps.utils.SessionManager

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(requireContext())

        binding.addStory.setOnClickListener {
            navigateToAddStory()
        }

        binding.actionLogout.setOnClickListener {
            logoutUser()
        }

        return binding.root
    }

    private fun navigateToAddStory() {
        val intent = Intent(requireContext(), AddStoryActivity::class.java)
        startActivity(intent)
    }

    private fun logoutUser() {
        sessionManager.clearLoginSession()
        Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()

        val intent = Intent(requireContext(), StarterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}