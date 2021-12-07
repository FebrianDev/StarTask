package com.febrian.startask

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.febrian.startask.databinding.ActivityMainBinding
import com.febrian.startask.ui.auth.CreateFamilyActivity
import com.febrian.startask.ui.auth.JoinFamilyActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.create.setOnClickListener(this)
        binding.join.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            binding.create -> {
                targetIntent(CreateFamilyActivity())
            }
            binding.join -> {
                targetIntent(JoinFamilyActivity())
            }
        }
    }

    private fun targetIntent(activity: AppCompatActivity) {
        val intent = Intent(applicationContext, activity::class.java)
        startActivity(intent)
    }
}