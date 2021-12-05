package com.io.whatsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.App

class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHostFragment = supportFragmentManager.findFragmentByTag("navHostFragment") as NavHostFragment
        navController = navHostFragment.navController

        setUpHomeScreenIfLoggedIn()
    }


    private fun setUpHomeScreenIfLoggedIn(){
        if (App.getUid().isNullOrEmpty()) return

        val graph = navHostFragment.navController.navInflater.inflate(R.navigation.nav_graph)
        graph.startDestination = R.id.homeFragment
        navController.graph = graph
    }




}