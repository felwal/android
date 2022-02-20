package me.felwal.android.demo

import androidx.appcompat.app.AppCompatDelegate
import me.felwal.android.util.FLog

val log = FLog("Demo")

fun updateDayNight(day: Boolean) = AppCompatDelegate.setDefaultNightMode(
    if (day) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
)