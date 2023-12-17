package com.capstone.naghamalquran.ui.prediction.api.retrofit

import com.capstone.naghamalquran.ui.prediction.api.response.ApiResponse2
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("predict?file")
    fun addAudio(
        @Part("notes") notes: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<ApiResponse2>
}