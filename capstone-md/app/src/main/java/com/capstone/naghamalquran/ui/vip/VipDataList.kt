package com.capstone.naghamalquran.ui.vip

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VipDataList (
    val nagham_type_vip : String,
    val short_desc_vip : String,
    val nagham_pict_vip: Int,
    val long_desc_vip: String
): Parcelable