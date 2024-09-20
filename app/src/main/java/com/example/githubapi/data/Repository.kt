package com.example.githubapi.data

import com.example.githubapi.utils.NetworkResult
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow

@ActivityRetainedScoped
interface Repository {
    suspend fun getUsers(): Flow<NetworkResult<List<User>>>
    suspend fun getDetailedInfo(userName: String): Flow<NetworkResult<DetailedInfo>>
}