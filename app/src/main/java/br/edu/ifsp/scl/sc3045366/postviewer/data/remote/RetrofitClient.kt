package br.edu.ifsp.scl.sc3045366.postviewer.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// conexão com a api jsonplaceholder
object RetrofitClient {
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
