package com.synapse.joyers.apiData

import android.content.Context
import com.synapse.joyers.apiData.response.Post
import com.synapse.joyers.utils.NetWorkResult
import kotlinx.coroutines.flow.Flow

class Repository(private val remoteDataSource: RemoteDataSource) {

    suspend fun getPostList(context: Context): Flow<NetWorkResult<List<Post>>> {
        return toResultFlow(context){
            remoteDataSource.getPosts()
        }
    }

}