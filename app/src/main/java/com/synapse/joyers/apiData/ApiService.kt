package com.synapse.joyers.apiData

import com.synapse.joyers.apiData.response.Post
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>
}