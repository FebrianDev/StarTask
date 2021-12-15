package com.febrian.startask.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.febrian.startask.ui.child.ChildHomeActivity
import com.febrian.startask.ui.parent.ParentHomeActivity
import com.febrian.startask.databinding.ActivityCreateFamilyBinding
import com.febrian.startask.utils.Constant
import com.febrian.startask.utils.CreateFamilyId
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CreateFamilyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateFamilyBinding
    var role = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateFamilyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val roles = arrayListOf(Constant.FATHER, Constant.MOTHER, Constant.SON, Constant.DAUGHTER)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, roles
        )

        binding.role.adapter = adapter

        binding.role.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                role = roles[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.create.setOnClickListener {
            val name = binding.name.text.toString()
            val familyId = CreateFamilyId.get()

            val database =
                FirebaseDatabase.getInstance(Constant.URL).reference.child(
                    "Family"
                ).child(familyId)

            if (name.isEmpty()) {
                binding.name.error = "Name must be filled"
            } else {
                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.child("familyId").setValue(familyId)
                        addRole(role, name, snapshot)

                        //goto parent home
                        if (role == Constant.MOTHER || role == Constant.FATHER) {
                            targetIntent(ParentHomeActivity(), name, familyId, role)
                        } else {
                            targetIntent(ChildHomeActivity(), name, familyId, role)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
                    }

                })
            }
        }
    }

    private fun targetIntent(activity: AppCompatActivity, name: String, id: String, role: String) {
        val intent = Intent(applicationContext, activity::class.java)
        val shared = getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        val editor = shared.edit()
        editor.putString(Constant.FAMILY_ID, id)
        editor.putString(Constant.ROLE, role)
        editor.putString(Constant.KEY_NAME, name)
        editor.apply()
        startActivity(intent)
    }

    private fun addRole(role: String, name: String, snapshot: DataSnapshot) {
        if (role == Constant.SON || role == Constant.DAUGHTER) {
            snapshot.ref.child("Child").child(name).child("name").setValue(name)
            snapshot.ref.child("Child").child(name).child("coin").setValue(0)
        } else {
            if (!snapshot.hasChild(role))
                snapshot.ref.child(role).setValue(name)
        }
    }
}