package com.febrian.startask.parent

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.febrian.startask.databinding.ActivityParentBinding
import com.febrian.startask.utils.Constant

class ParentActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityParentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.father.setOnClickListener(this)
        binding.mother.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.father -> {
                targetIntent(CreateFamilyActivity(), Constant.FATHER)
            }

            binding.mother -> {
                targetIntent(CreateFamilyActivity(), Constant.MOTHER)
            }
        }
    }

    private fun targetIntent(activity: AppCompatActivity, role: String) {
        val intent = Intent(applicationContext, activity::class.java)
        intent.putExtra(Constant.ROLE, role)
        startActivity(intent)
    }
}