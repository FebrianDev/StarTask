package com.febrian.startask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.febrian.startask.databinding.ActivityMainBinding
import com.febrian.startask.parent.ParentActivity
import com.febrian.startask.parent.ParentFamilyActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.parent.setOnClickListener(this)
        binding.child.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v){
            binding.parent ->{
                targetIntent(ParentFamilyActivity())
            }
        }
    }

    private fun targetIntent(activity: AppCompatActivity){
        val intent = Intent(applicationContext, activity::class.java)
        startActivity(intent)
    }
}