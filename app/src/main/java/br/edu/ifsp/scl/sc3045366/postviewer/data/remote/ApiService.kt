package br.edu.ifsp.scl.sc3045366.postviewer.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("posts/{id}/comments")
    suspend fun getComments(@Path("id") postId: Int): List<ApiComment>
}
