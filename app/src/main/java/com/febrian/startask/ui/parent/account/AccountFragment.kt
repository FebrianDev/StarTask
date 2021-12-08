package com.febrian.startask.ui.parent.account

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.febrian.startask.MainActivity
import com.febrian.startask.R
import com.febrian.startask.databinding.FragmentAccountBinding
import com.febrian.startask.ui.parent.task.CreateTaskActivity
import com.febrian.startask.utils.Constant

class AccountFragment : Fragment() {


    private lateinit var binding : FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = this.requireActivity().getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        val role: String? = preferences.getString(Constant.ROLE, null)
        val familyId: String? = preferences.getString(Constant.FAMILY_ID, null)

        binding.tvTaskFamilyId.text = familyId
        binding.tvTaskRole.text = role

        binding.logout.setOnClickListener {
            preferences.edit().clear().apply()
            val mIntent = Intent(activity, MainActivity::class.java)
            startActivity(mIntent)
            requireActivity().finish()
        }
    }
}