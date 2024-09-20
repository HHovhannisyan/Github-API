package com.example.githubapi.data

import com.google.gson.annotations.SerializedName

data class DetailedInfo(
    @SerializedName("name") val name: String,
    @SerializedName("location") val location: String,
    @SerializedName("followers") val followers: Int,
    @SerializedName("following") val following: Int,
    @SerializedName("bio") val bio: String,
    @SerializedName("public_repos") val publicRepos: Int,
    @SerializedName("public_gists") val publicGists: Int,
    @SerializedName("updated_at") val updatedAt: String
)