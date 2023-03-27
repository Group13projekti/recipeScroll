package com.example.recipescroll_v022

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception

class FeedActivity: AppCompatActivity()  {

    lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feed_activity)
        replacefragment(FrontPage())

        bottomNavigationView = findViewById(R.id.bottomNavigationView) as BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {

                R.id.FrontPage -> {
                    replacefragment(FrontPage())
                    true
                }
                R.id.favorites -> {
                    replacefragment(favorites())
                    true
                }
                R.id.profile -> {
                    replacefragment(profile())
                    true
                }

                else -> {
                    true
                }
            }
        }
    }

    private fun replacefragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }
}