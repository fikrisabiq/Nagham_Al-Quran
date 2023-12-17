package com.capstone.naghamalquran.ui.prediction.api.response

import com.google.gson.annotations.SerializedName

data class ApiResponse2(

	@field:SerializedName("file_url")
	val fileUrl: String?,

	@field:SerializedName("message")
	val message: String?
)
