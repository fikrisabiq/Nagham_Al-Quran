package com.capstone.naghamalquran.ui.type

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NaghamList (
    val nagham_type : String,
    val short_desc : String,
    val nagham_pict: Int,
    val long_desc: String
): Parcelable