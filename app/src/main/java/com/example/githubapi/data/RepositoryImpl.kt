package com.example.githubapi.data

import com.example.githubapi.data.remote.RemoteDataSource
import com.example.githubapi.utils.NetworkResult


import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class RepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    Repository, BaseApiResponse() {
    override suspend fun getUsers(): Flow<NetworkResult<List<User>>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getUsers() })
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getDetailedInfo(userName: String): Flow<NetworkResult<DetailedInfo>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getDetailedInfo(userName) })
        }.flowOn(Dispatchers.IO)
    }
}