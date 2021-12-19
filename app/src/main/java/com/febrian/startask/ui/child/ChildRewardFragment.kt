package com.febrian.startask.ui.child

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.febrian.startask.R
import com.febrian.startask.data.Reward
import com.febrian.startask.databinding.FragmentChildRewardBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChildRewardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getRewardData()
    }

    private fun getRewardData() { //next merubah ke viewModel
        val preferences = this.requireActivity()
            .getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        val familyId: String? = preferences.getString(Constant.FAMILY_ID, "")
        val childName = preferences.getString(Constant.KEY_NAME, "").toString()
        childTaskRecyclerView = binding.rvShowAllReward
        childTaskRecyclerView.layoutManager = LinearLayoutManager(context)
        childTaskRecyclerView.setHasFixedSize(true)

        childTaskArrayList = ArrayList()
        childTaskArrayList.clear()

        dbref = FirebaseDatabase.getInstance(Constant.URL).reference.child("Family")
            .child(familyId.toString())
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.coin.text = "Coin " + snapshot.child("Child").child(childName)
                    .child("coin").value.toString()
                for (familySnapshot in snapshot.child("Reward").children) {
                    val family = familySnapshot.getValue(Reward::class.java)!!
                    childTaskArrayList.add(family)
                    Log.d("DATA", family.name.toString())
                }
                val adapter = ChildRewardAdapter(childTaskArrayList, snapshot.child("Child").child(childName)
                    .child("coin").value.toString().toInt(), familyId.toString(), childName)
                childTaskRecyclerView.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.reward_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_reward -> {
                val intent = Intent(requireActivity(), RewardActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}