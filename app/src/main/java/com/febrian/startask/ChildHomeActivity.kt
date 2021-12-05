package com.febrian.startask

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.febrian.startask.databinding.ActivityChildHomeBinding
import com.febrian.startask.utils.Constant

class ChildHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChildHomeBinding
    //val role = intent.getStringExtra(Constant.ROLE).toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChildHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView

        saveData()//sementara

        val navController = findNavController(R.id.nav_host_fragment_activity_child_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_child_reward, R.id.navigation_child_history, R.id.navigation_child_task
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun saveData() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        val role = sharedPreferences.getString(Constant.ROLE, "")
        val familyId = sharedPreferences.getString(Constant.FAMILY_ID, "")

        Toast.makeText(this,"tersimpan", Toast.LENGTH_SHORT).show()
    }

}