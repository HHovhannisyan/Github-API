package com.example.githubapi.presentation.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.githubapi.data.User

object UsersDiffUtil : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.userName == newItem.userName

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem == newItem
}