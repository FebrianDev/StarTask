package com.febrian.startask.ui.child

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.febrian.startask.R
import com.febrian.startask.data.MyReward
import com.febrian.startask.data.Task
import com.febrian.startask.databinding.ActivityRewardBinding
import com.febrian.startask.databinding.FragmentChildTaskBinding
import com.febrian.startask.ui.parent.task.ParentTaskAdapter
import com.febrian.startask.utils.Constant
import com.google.firebase.database.*

class RewardActivity : AppCompatActivity() {

    private lateinit var dbref: DatabaseReference
    private lateinit var childTaskRecyclerView: RecyclerView
    private lateinit var childTaskArrayList: ArrayList<MyReward>

    private lateinit var preferences: SharedPreferences
    private lateinit var familyId: String
    private lateinit var childName: String

    private lateinit var binding: ActivityRewardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Reward"

        preferences = applicationContext
            .getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        familyId = preferences.getString(Constant.FAMILY_ID, "").toString()
        childName = preferences.getString(Constant.KEY_NAME, "").toString()

        childTaskRecyclerView = binding.rvShowMyReward
        childTaskRecyclerView.layoutManager = LinearLayoutManager(this)
        childTaskRecyclerView.setHasFixedSize(true)

        childTaskArrayList = ArrayList()

        initAction(familyId)
        dbref = FirebaseDatabase.getInstance(Constant.URL).reference.child("Family")
            .child(familyId).child("Child").child(childName).child("Reward")

        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (s in snapshot.children) {
                    val data = s.getValue(MyReward::class.java)
                    if (data != null) {
                        childTaskArrayList.add(data)
                    }
                }

                val adapter = MyRewardAdapter(childTaskArrayList, familyId, childName)
                childTaskRecyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun initAction(familyId: String) {
        var myReward: MyReward? = null
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                myReward = (viewHolder as MyRewardAdapter.ViewHolder).myReward
                if (myReward?.complete == true)
                    return makeMovementFlags(0, ItemTouchHelper.RIGHT)
                else
                    return 0
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val reference =
                    FirebaseDatabase.getInstance(Constant.URL).reference.child("Family")
                        .child(familyId).child("Child").child(childName).child("Reward")
                reference.child(myReward?.id.toString()).removeValue()
            }

        })
        itemTouchHelper.attachToRecyclerView(childTaskRecyclerView)
    }

    override fun onResume() {
        super.onResume()

        initAction(familyId)
    }
}