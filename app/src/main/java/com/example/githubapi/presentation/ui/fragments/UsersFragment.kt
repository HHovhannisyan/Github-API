package com.example.githubapi.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.githubapi.R
import com.example.githubapi.presentation.ui.adapter.UsersAdapter
import com.example.githubapi.data.User
import com.example.githubapi.databinding.FragmentUsersBinding
import com.example.githubapi.utils.NetworkConnectivityObserver
import com.example.githubapi.utils.NetworkResult
import com.example.githubapi.presentation.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UsersFragment : Fragment(), UsersAdapter.OnClickInterface {
    private lateinit var connectivityObserver: NetworkConnectivityObserver

    private lateinit var binding: FragmentUsersBinding
    private val usersRVAdapter by lazy {
        UsersAdapter(this)
    }
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        connectivityObserver = NetworkConnectivityObserver(requireActivity())

        val dividerItemDecoration =
            DividerItemDecoration(binding.recyclerView.context, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(
            requireActivity(),
            R.drawable.divider
        )?.let {
            dividerItemDecoration.setDrawable(
                it
            )
        }
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        viewLifecycleOwner.lifecycleScope.launch {
            fetchUsers()
        }

        binding.searchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                usersRVAdapter.filter.filter(newText)
                return true
            }
        })

        return binding.root
    }

    private suspend fun fetchUsers() {
        mainViewModel.fetchUserResponse()
        mainViewModel.responseUser.collect { response ->
            when (response) {
                is NetworkResult.Success<*> -> {
                    binding.progressCircular.visibility=View.GONE
                    binding.recyclerView.adapter = usersRVAdapter
                    usersRVAdapter.submitList(response.data)
                    response.data?.let { usersRVAdapter.setData(it.toMutableList()) }
                }

                is NetworkResult.Error -> Toast.makeText(
                    requireActivity(),
                    response.message,
                    Toast.LENGTH_SHORT
                ).show()

                is NetworkResult.Loading -> {}
            }
        }
    }

    companion object {
        fun newInstance() = UsersFragment()
    }

    override fun onItemClick(user: User) {

        val detailedInfoFragment = DetailedInfoFragment.newInstance()
        val args = Bundle()
        args.apply {
            putString(
                "userName", user.userName
            )
            putString(
                "avatarUrl", user.avatarUrl
            )
        }

        detailedInfoFragment.arguments = args
        lifecycleScope.launch {
            connectivityObserver.observe().collect {
                if (it.toString() == "Available") {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.main, detailedInfoFragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss()
                } else {
                    Toast.makeText(requireActivity(), "No Internet connection", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}