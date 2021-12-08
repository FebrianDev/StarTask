package com.febrian.startask.ui.detail

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.febrian.startask.databinding.ActivityDetailRewardBinding
import com.febrian.startask.utils.Constant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailRewardActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailRewardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRewardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val reward = intent.getStringExtra(Constant.KEY_REWARD).toString()
        val preferences = getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        val familyId: String? = preferences.getString(Constant.FAMILY_ID, "")

        val reference = FirebaseDatabase.getInstance(Constant.URL).reference.child("Family")
            .child(familyId.toString()).child("Reward")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.taskName.setText(snapshot.child(reward).child("name").value.toString())
                binding.amountStar.setText(snapshot.child(reward).child("amount").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        binding.save.setOnClickListener {

            reference.child(reward).removeValue()

            val taskName = binding.taskName.text.toString()
            val amount = binding.amountStar.text.toString()

            if (taskName.isEmpty()) {
                binding.taskName.error = "Reward Name must be filled"
            } else if (amount.isEmpty()) {
                binding.amountStar.error = "Amount must be filled"
            } else {
                reference.child(taskName)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.ref.child("name")
                                .setValue(taskName)
                            snapshot.ref.child("amount")
                                .setValue(amount)

                            onBackPressed()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG)
                                .show()
                        }

                    })
            }
        }

        binding.delete.setOnClickListener {
            reference.child(reward).removeValue()
            onBackPressed()
        }
    }
}