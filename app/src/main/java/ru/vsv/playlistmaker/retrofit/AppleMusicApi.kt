package ru.vsv.playlistmaker.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.vsv.playlistmaker.dto.SearchTracksResponseDto

interface AppleMusicApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<SearchTracksResponseDto>
}