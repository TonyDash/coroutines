package com.tiga.coroutines.retrofitApi

import com.tiga.coroutines.entity.Repo
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {
    @GET("users/{user}/repos")
    fun listRepos(@Path("user")user:String):Call<List<Repo>>
    @GET("users/{user}/repos")
    suspend fun listReposKt(@Path("user")user:String):List<Repo>
    @GET("users/{user}/repos")
    suspend fun listReposRx(@Path("user")user:String):Single<List<Repo>>
}