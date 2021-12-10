package com.febrian.startask.ui.child

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.febrian.startask.data.Reward
import com.febrian.startask.databinding.FragmentChildRewardBinding
import com.febrian.startask.ui.parent.reward.RewardAdapter
import com.febrian.startask.utils.Constant
import com.google.firebase.database.*

class ChildRewardFragment : Fragment() {
    private var _binding: FragmentChildRewardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var dbref: DatabaseReference
    private lateinit var childTaskRecyclerView: RecyclerView
    private lateinit var childTaskArrayList: ArrayList<Reward>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChildRewardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getRewardData()
    }

    private fun getRewardData() { //next merubah ke viewModel
        val preferences = this.requireActivity()
            .getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        val familyId: String? = preferences.getString(Constant.FAMILY_ID, "")

        childTaskRecyclerView = binding.rvShowAllReward
        childTaskRecyclerView.layoutManager = LinearLayoutManager(context)
        childTaskRecyclerView.setHasFixedSize(true)

        childTaskArrayList = ArrayList()
        childTaskArrayList.clear()

        dbref = FirebaseDatabase.getInstance(Constant.URL).reference.child("Family")
            .child(familyId.toString()).child("Reward")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (familySnapshot in snapshot.children) {
                    val family = familySnapshot.getValue(Reward::class.java)!!
                    childTaskArrayList.add(family)
                    Log.d("DATA", family.name.toString())
                }
                val adapter = RewardAdapter(childTaskArrayList)
                childTaskRecyclerView.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}