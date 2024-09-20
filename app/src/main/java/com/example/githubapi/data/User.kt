package com.example.githubapi.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login") val userName: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("id") val id: String
)
