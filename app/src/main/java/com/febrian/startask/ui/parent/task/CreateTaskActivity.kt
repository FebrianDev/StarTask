package com.febrian.startask.ui.parent.task

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.febrian.startask.databinding.ActivityCreateTaskBinding
import com.febrian.startask.ui.parent.ParentHomeActivity
import com.febrian.startask.utils.Constant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CreateTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTaskBinding

    //var familyId = ""
    var role = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Create Task"

        val listRole = ArrayList<String>()
        val familyId = getSharedPreferences(
            Constant.SharedPreferences,
            MODE_PRIVATE
        ).getString(Constant.FAMILY_ID, "").toString()
        val referenceGet =
            FirebaseDatabase.getInstance(Constant.URL).reference.child("Family").child(familyId)
        referenceGet.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.child("Child").children.forEach {
                    listRole.add(it.child("name").value.toString())
                }
                val adapter = ArrayAdapter(
                    applicationContext,
                    R.layout.simple_spinner_item, listRole
                )

                binding.role.adapter = adapter

                binding.role.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        Toast.makeText(applicationContext, listRole[p2], Toast.LENGTH_LONG).show()
                        role = listRole[p2]
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        binding.save.setOnClickListener {
            val taskName = binding.taskName.text.toString()
            val amount = binding.amountStar.text.toString()

            if (taskName.isEmpty()) {
                binding.taskName.error = "Task Name must be filled"
            } else if (amount.isEmpty()) {
                binding.amountStar.error = "Amount must be filled"
            } else {
                Toast.makeText(applicationContext, role.toString(), Toast.LENGTH_LONG).show()
                val referenceCreate =
                    FirebaseDatabase.getInstance(Constant.URL).reference.child("Family")
                        .child(familyId).child("Child").child(role)
                referenceCreate.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.child("task").child(taskName).child("name")
                            .setValue(taskName)
                        snapshot.ref.child("task").child(taskName).child("amount")
                            .setValue(amount)
                        snapshot.ref.child("task").child(taskName).child("complete")
                            .setValue("0")
                        //kembali ke parent home
                        targetIntent()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
                    }

                })
            }
        }
    }

    private fun targetIntent() {
        finish()
//        val mIntent = Intent(this, ParentHomeActivity::class.java)
//        startActivity(mIntent)
    }
}