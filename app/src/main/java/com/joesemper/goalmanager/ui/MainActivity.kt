package com.joesemper.goalmanager.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.joesemper.goalmanager.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun navigateTo(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragment_container_view, fragment)
            addToBackStack("Goals")
        }
    }

//    companion object {
//        fun getStartIntent(context: Context) = Intent(context, MainActivity::class.java)
//    }
}