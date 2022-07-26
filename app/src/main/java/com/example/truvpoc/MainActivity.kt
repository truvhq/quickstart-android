package com.example.truvpoc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.truvpoc.ui.MainFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openScreen(MainFragment())
    }

}

fun FragmentActivity.openScreen(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .replace(R.id.fragmentContainer, fragment)
        .commit()
}