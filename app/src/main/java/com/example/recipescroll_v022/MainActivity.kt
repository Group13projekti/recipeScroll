package com.example.recipescroll_v022

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var navControllerLogin: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment) as NavHostFragment
        navControllerLogin = navHostFragment.navController

        setupActionBarWithNavController(navControllerLogin)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navControllerLogin.navigateUp() || super.onSupportNavigateUp()
    }
}