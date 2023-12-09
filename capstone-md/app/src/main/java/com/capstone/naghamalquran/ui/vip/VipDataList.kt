package com.capstone.naghamalquran.ui.vip

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VipDataList (
    val nagham_type : String,
    val short_desc : String,
    val nagham_pict: Int,
    val long_desc: String,
    val arab: String,
    val audio_url: String
): Parcelable