package com.magarusik.criptoapp.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("CoinInfo")
    @Expose
    val coinInfo: CoinInfo? = null
)
