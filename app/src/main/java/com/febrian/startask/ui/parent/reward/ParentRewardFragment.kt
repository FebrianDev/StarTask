package com.febrian.startask.ui.parent.reward

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.febrian.startask.data.Reward
import com.febrian.startask.databinding.FragmentParentRewardBinding
import com.febrian.startask.utils.Constant
import com.febrian.startask.utils.Helper
import com.google.firebase.database.*

class ParentRewardFragment : Fragment() {
    private var _binding: FragmentParentRewardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var dbref: DatabaseReference
    private lateinit var parentTaskRecyclerView: RecyclerView
    private lateinit var parentTaskArrayList: ArrayList<Reward>

    private val _isLoading = MutableLiveData<Boolean>()
    private val isLoading: LiveData<Boolean> = _isLoading
    private val helper = Helper()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)

        isLoading.observe(this, { helper.showLoading(it, binding.progressBar)})
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentParentRewardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRewardData()

        binding.btnAddReward.setOnClickListener {
            val mIntent = Intent(activity, CreateRewardActivity::class.java)
            startActivity(mIntent)
        }
    }

    private fun getRewardData() { //next merubah ke viewModel
        _isLoading.value = true
        val preferences = this.requireActivity()
            .getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        val familyId: String? = preferences.getString(Constant.FAMILY_ID, "")

        parentTaskRecyclerView = binding.rvShowAllReward
        parentTaskRecyclerView.layoutManager = LinearLayoutManager(context)
        parentTaskRecyclerView.setHasFixedSize(true)

        parentTaskArrayList = ArrayList()
        parentTaskArrayList.clear()

        dbref = FirebaseDatabase.getInstance(Constant.URL).reference.child("Family")
            .child(familyId.toString()).child("Reward")
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _isLoading.value = false
                for (familySnapshot in snapshot.children) {
                    val family = familySnapshot.getValue(Reward::class.java)!!
                    parentTaskArrayList.add(family)
                    Log.d("DATA", family.name.toString())
                }
                val adapter = RewardAdapter(parentTaskArrayList)
                parentTaskRecyclerView.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                _isLoading.value = false
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onResume() {
        super.onResume()
        parentTaskArrayList.clear()
        getRewardData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}