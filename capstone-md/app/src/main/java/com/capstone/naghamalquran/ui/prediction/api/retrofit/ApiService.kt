package com.capstone.naghamalquran.ui.prediction.api.retrofit

import com.capstone.naghamalquran.ui.prediction.api.response.ApiResponse2
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    companion object {
        const val API_KEY_HEADER = "API-Key: THhUy6ioBEOHKunkb0RIyqfUWiJQSgpA9JiRAsSnclRVmIVkZmkLIK6uuao7FSq2"
    }

    @Multipart
    @POST("predict")
    @Headers(API_KEY_HEADER)
    fun addAudio(
        @Part audio: MultipartBody.Part
    ): Call<ApiResponse2>
}


