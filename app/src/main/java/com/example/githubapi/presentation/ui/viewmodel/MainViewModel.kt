package com.example.githubapi.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapi.data.DetailedInfo
import com.example.githubapi.data.User
import com.example.githubapi.usecase.UsersUseCase
import com.example.githubapi.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getUsersUseCase: UsersUseCase) : ViewModel() {
    private val _responseUser: MutableSharedFlow<NetworkResult<List<User>>> =
        MutableSharedFlow(replay = 0)
    val responseUser: MutableSharedFlow<NetworkResult<List<User>>> = _responseUser

    private val _responseDetailedInfo: MutableSharedFlow<NetworkResult<DetailedInfo>> =
        MutableSharedFlow(replay = 0)
    val responseDetailedInfo: MutableSharedFlow<NetworkResult<DetailedInfo>> = _responseDetailedInfo

    fun fetchUserResponse() = viewModelScope.launch {
        getUsersUseCase.getUsers().collect { values ->
            _responseUser.emit(values)
        }
    }

    fun fetchDetailedInfoResponse(userName: String) = viewModelScope.launch {
        getUsersUseCase.getDetailedInfo(userName).collect { values ->
            _responseDetailedInfo.emit(values)
        }
    }
}