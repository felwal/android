package com.felwal.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.felwal.android.util.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toast("Hello World!")
    }
}