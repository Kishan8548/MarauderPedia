package com.example.marauderspedia.api

import com.example.marauderspedia.models.Character
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("characters")
    fun getCharacters(): Call<List<Character>>
}
