package com.febrian.startask.ui.parent.reward

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.febrian.startask.databinding.ActivityCreateRewardBinding
import com.febrian.startask.utils.Constant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CreateRewardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateRewardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateRewardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.save.setOnClickListener {
            val taskName = binding.rewardName.text.toString()
            val amount = binding.amountStar.text.toString()

            if (taskName.isEmpty()) {
                binding.rewardName.error = "Reward Name must be filled"
            } else if (amount.isEmpty()) {
                binding.amountStar.error = "Amount must be filled"
            } else {
                val referenceCreate =
                    FirebaseDatabase.getInstance(Constant.URL).reference.child("Family")
                        .child("i6NaAzz").child("Reward").child(taskName)
                referenceCreate.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.child("name")
                            .setValue(taskName)
                        snapshot.ref.child("amount")
                            .setValue(amount)

                        onBackPressed()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
                    }

                })
            }
        }
    }
}