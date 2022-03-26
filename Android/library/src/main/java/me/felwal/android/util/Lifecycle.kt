package me.felwal.android.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

inline fun <reified A : AppCompatActivity> Activity.launchActivity() =
    launchActivity(A::class)

fun Activity.launchActivity(cls: KClass<*>) =
    launchActivity(cls.java)

fun Activity.launchActivity(cls: Class<*>) {
    val intent = Intent(this, cls)
    startActivity(intent)
}

fun Context.openLink(link: String) {
    try {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
    catch (e: ActivityNotFoundException) {
        toast("Invalid link: $link")
        log.e("Invalid link: $link")
    }
}

val Context.layoutInflater: LayoutInflater get() = LayoutInflater.from(this)