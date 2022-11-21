package com.magarusik.criptoapp.utils

import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun convertTimestampToTime(timestamp: Long?): String {
    if (timestamp == null)
        return ""
    val date = Date(Timestamp(timestamp * 1000).time)
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}