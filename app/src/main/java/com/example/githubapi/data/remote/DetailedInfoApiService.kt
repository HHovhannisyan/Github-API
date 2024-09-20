package com.example.githubapi.data.remote

import com.example.githubapi.data.DetailedInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailedInfoApiService {
    @GET("{userName}")
    suspend fun getDetailedInfo(@Path("userName") userName: String): Response<DetailedInfo>
}