package com.example.githubapi.di


import com.example.githubapi.data.Repository
import com.example.githubapi.data.RepositoryImpl
import com.example.githubapi.data.remote.DetailedInfoApiService
import com.example.githubapi.data.remote.RemoteDataSource
import com.example.githubapi.data.remote.UsersApiService
import com.example.githubapi.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.cache.CacheInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @LoggingInterceptor
    fun provideLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @OtherInterceptor
    fun provideOtherInterceptor(): Interceptor =
        CacheInterceptor(null)

    @Singleton
    @Provides
    fun provideHttpClient(@LoggingInterceptor loggingInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .callTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build()
    }

    @Singleton
    @Provides
    @Named("Users")
    fun provideUsersRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.USERS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @Named("DetailedInfo")
    fun provideDetailedInfoRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.INFO_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideUsersService(@Named("Users") retrofit: Retrofit): UsersApiService =
        retrofit.create(UsersApiService::class.java)

    @Singleton
    @Provides
    fun provideDetailedUserService(@Named("DetailedInfo") retrofit: Retrofit): DetailedInfoApiService =
        retrofit.create(DetailedInfoApiService::class.java)

    @Singleton
    @Provides
    fun providesRepository(remoteDataSource: RemoteDataSource): Repository =
        RepositoryImpl(remoteDataSource)
}