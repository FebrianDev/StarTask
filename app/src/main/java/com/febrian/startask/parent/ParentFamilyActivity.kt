package com.febrian.startask.parent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.febrian.startask.R
import com.febrian.startask.databinding.ActivityParentFamilyBinding

class ParentFamilyActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityParentFamilyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentFamilyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.create.setOnClickListener(this)
        binding.join.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            binding.create -> { targetIntent(ParentActivity()) }
        }
    }

    private fun targetIntent(activity: AppCompatActivity) {
        val intent = Intent(applicationContext, activity::class.java)
        startActivity(intent)
    }
}