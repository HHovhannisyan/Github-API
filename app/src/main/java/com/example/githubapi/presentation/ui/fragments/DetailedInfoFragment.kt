package com.example.githubapi.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.githubapi.R
import com.example.githubapi.databinding.FragmentDetailedInfoBinding
import com.example.githubapi.presentation.ui.viewmodel.MainViewModel
import com.example.githubapi.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailedInfoFragment : Fragment() {
    private lateinit var binding: FragmentDetailedInfoBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailedInfoBinding.inflate(inflater, container, false)
        val mArgs = arguments

        mArgs?.let {
            val userName = mArgs.getString("userName").toString()
            val avatarUrl = mArgs.getString("avatarUrl").toString()
            viewLifecycleOwner.lifecycleScope.launch {
                fetchDetailedInfo(userName, avatarUrl)
            }
        }

        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return binding.root
    }

    private suspend fun fetchDetailedInfo(userName: String, avatarUrl: String) {
        mainViewModel.fetchDetailedInfoResponse(userName)
        mainViewModel.responseDetailedInfo.collect { response ->
            when (response) {
                is NetworkResult.Success -> {
                    println("Response.data: " + response.data)
                    binding.apply {
                        img.load(avatarUrl) {
                            placeholder(R.mipmap.ic_launcher)
                            error(R.mipmap.ic_launcher)
                        }

                        tvUserName.text = response.data?.name
                        tvLocation.text = response.data?.location
                        tvFollowers.text = "${response.data?.followers} Followers"
                        tvFollowing.text = "${response.data?.following} Following"

                        if (!response.data?.bio.isNullOrBlank()) {
                            tvBio.text = response.data?.bio
                        } else {
                            llcBio.visibility = View.GONE
                        }

                        if (response.data?.publicRepos != 0) {
                            tvRepository.text = "${response.data?.publicRepos}"
                        } else {
                            llcRepository.visibility = View.GONE
                        }

                        if (response.data?.publicGists != 0) {
                            tvGists.text = "${response.data?.publicGists}"
                        } else {
                            llcGists.visibility = View.GONE
                        }

                        if (!response.data?.updatedAt.isNullOrBlank()) {
                            tvUpdatedDate.text = response.data?.updatedAt
                        } else {
                            llcUpdated.visibility = View.GONE
                        }
                    }
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireActivity(), response.message, Toast.LENGTH_SHORT).show()
                    println("response.message:" + response.message)
                }

                is NetworkResult.Loading -> {
                }
            }
        }
    }

    companion object {
        fun newInstance() = DetailedInfoFragment()
    }
}