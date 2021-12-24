package com.febrian.startask.ui.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.febrian.startask.databinding.ActivityDetailTaskBinding
import com.febrian.startask.utils.Constant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val task = intent.getStringExtra(Constant.KEY_TASK).toString()
        val name = intent.getStringExtra(Constant.KEY_NAME).toString()
        val preferences = getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        val familyId: String? = preferences.getString(Constant.FAMILY_ID, "")

        val reference = FirebaseDatabase.getInstance(Constant.URL).reference.child("Family")
            .child(familyId.toString()).child("Child").child(name).child("task")

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Edit Task"

        reference.child(task).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.taskName.setText(snapshot.child("name").value.toString())
                binding.amountStar.setText(snapshot.child("amount").value.toString())

                Log.d("Name",snapshot.child("name").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        binding.save.setOnClickListener {

            reference.child(task).removeValue()

            val taskName = binding.taskName.text.toString()
            val amount = binding.amountStar.text.toString()

            if (taskName.isEmpty()) {
                binding.taskName.error = "Task Name must be filled"
            } else if (amount.isEmpty()) {
                binding.amountStar.error = "Amount must be filled"
            } else {
                reference.child(taskName)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.ref.child("name")
                                .setValue(taskName)
                            snapshot.ref.child("amount")
                                .setValue(amount)
                            snapshot.ref.child("complete").setValue("0")

                            finish()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG)
                                .show()
                        }

                    })
            }
        }

        binding.delete.setOnClickListener {
            reference.child(task).removeValue()
            finish()
        }
    }
}