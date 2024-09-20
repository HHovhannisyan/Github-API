package com.example.githubapi.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.githubapi.R
import com.example.githubapi.databinding.ActivityMainBinding
import com.example.githubapi.presentation.ui.fragments.UsersFragment
import com.example.githubapi.utils.NetworkConnectivityObserver
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var connectivityObserver: NetworkConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple)
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        lifecycleScope.launch {
            connectivityObserver.observe().collect { status ->
                if (status.toString() == "Available") {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.main, UsersFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                } else {
                    Snackbar.make(
                        binding.main,
                        "No Internet connection",
                        Snackbar.ANIMATION_MODE_SLIDE
                    )
                        .show()
                }
            }
        }
    }
}