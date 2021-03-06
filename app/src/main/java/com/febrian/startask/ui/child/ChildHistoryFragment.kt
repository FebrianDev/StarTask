package com.febrian.startask.ui.child

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.febrian.startask.databinding.FragmentChildHistoryBinding
import com.febrian.startask.utils.Constant

class ChildHistoryFragment : Fragment() {
    private var _binding: FragmentChildHistoryBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChildHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val preferences = this.requireActivity().getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        val role: String? = preferences.getString(Constant.ROLE, null)
        val familyId: String? = preferences.getString(Constant.FAMILY_ID, null)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}