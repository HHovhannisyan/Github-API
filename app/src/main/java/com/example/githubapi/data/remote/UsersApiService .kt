package com.example.githubapi.data.remote

import com.example.githubapi.data.User
import retrofit2.Response
import retrofit2.http.GET


interface UsersApiService {
    @GET("users")
    suspend fun getUsers(): Response<List<User>>
}
