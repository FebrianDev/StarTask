package com.febrian.startask

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.febrian.startask.databinding.ActivityParentHomeBinding
import com.febrian.startask.utils.Constant
import io.paperdb.Paper

class ParentHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParentHomeBinding
    //val role = intent.getStringExtra(Constant.ROLE).toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val role = intent.getStringExtra(Constant.ROLE).toString()
        val familyId = intent.getStringExtra(Constant.FAMILY_ID).toString()

        //menyimpan variabel kedalam memori
        Paper.init(this)
        Paper.book().write(Constant.FAMILY_ID, familyId)
        Paper.book().write(Constant.ROLE, role)



        binding = ActivityParentHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //targetIntent(FamilyActivity(), familyId, role)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_parent_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_parent_reward, R.id.navigation_parent_home, R.id.navigation_parent_task
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

}