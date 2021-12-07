package com.febrian.startask.ui.parent.reward

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.febrian.startask.databinding.FragmentParentRewardBinding
import com.febrian.startask.ui.parent.task.CreateTaskActivity
import com.febrian.startask.utils.Constant

class ParentRewardFragment : Fragment() {
    private var _binding: FragmentParentRewardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentParentRewardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val preferences = this.requireActivity().getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        val role: String? = preferences.getString(Constant.ROLE, null)
        val familyId: String? = preferences.getString(Constant.FAMILY_ID, null)
        binding.tvTaskFamilyId.text = familyId
        binding.tvTaskRole.text = role

        binding.btnAddReward.setOnClickListener{
            val mIntent = Intent(activity, CreateTaskActivity::class.java)
            startActivity(mIntent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}