package com.example.githubapi.data.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: UsersApiService,
    private val detaildInfoApiService: DetailedInfoApiService
) {
    suspend fun getUsers() = apiService.getUsers()
    suspend fun getDetailedInfo(userName: String) = detaildInfoApiService.getDetailedInfo(userName)
}
