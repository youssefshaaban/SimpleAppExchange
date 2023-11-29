package com.paypay.taskapp.data.util


fun isDateStale(lastFetchedTime: Long): Boolean {
    val currentTime = System.currentTimeMillis()
    val timeDifference = currentTime - lastFetchedTime
    val thirtyMinutesInMillis = 30 * 60 * 1000 // 30 minutes in milliseconds

    return timeDifference > thirtyMinutesInMillis
}