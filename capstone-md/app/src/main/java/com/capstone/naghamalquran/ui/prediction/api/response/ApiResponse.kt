package com.capstone.naghamalquran.ui.prediction.api.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ApiResponse(

	@field:SerializedName("file_url")
	val fileUrl: String,

	@field:SerializedName("message")
	val message: String
) : Parcelable
