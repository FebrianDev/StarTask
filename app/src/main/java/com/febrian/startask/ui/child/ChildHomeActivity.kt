package com.febrian.startask.ui.child

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.febrian.startask.R
import com.febrian.startask.databinding.ActivityChildHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ChildHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChildHomeBinding
    //val role = intent.getStringExtra(Constant.ROLE).toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChildHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_child_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_child_task,
                R.id.navigation_child_reward,
                R.id.navigation_child_account
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

}