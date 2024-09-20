package com.example.githubapi.usecase

import com.example.githubapi.data.Repository
import javax.inject.Inject

class UsersUseCase @Inject constructor(private val repository: Repository) {
    suspend fun getUsers() = repository.getUsers()
    suspend fun getDetailedInfo(userName: String) = repository.getDetailedInfo(userName)
}