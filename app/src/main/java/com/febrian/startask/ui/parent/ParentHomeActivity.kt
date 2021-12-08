package com.febrian.startask.ui.parent

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
import com.febrian.startask.R
import com.febrian.startask.databinding.ActivityParentHomeBinding
import com.febrian.startask.utils.Constant

class ParentHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParentHomeBinding
    //val role = intent.getStringExtra(Constant.ROLE).toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityParentHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        saveData()//SharedPreferences sementara

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_parent_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_parent_reward,
                R.id.navigation_parent_account,
                R.id.navigation_parent_task
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun saveData() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        val role = sharedPreferences.getString(Constant.ROLE, "")
        val familyId = sharedPreferences.getString(Constant.FAMILY_ID, "")

        Toast.makeText(this, "tersimpan", Toast.LENGTH_SHORT).show()
    }


}