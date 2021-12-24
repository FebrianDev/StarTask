package com.febrian.startask.ui.auth

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.febrian.startask.databinding.ActivityJoinFamilyBinding
import com.febrian.startask.ui.child.ChildHomeActivity
import com.febrian.startask.ui.parent.ParentHomeActivity
import com.febrian.startask.utils.Constant
import com.febrian.startask.utils.Roles
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class JoinFamilyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinFamilyBinding
    var role = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinFamilyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSimpleSpinner()

        setupCustomSpinner()

        binding.join.setOnClickListener {
            val familyId = binding.familyId.text.toString()
            val name = binding.name.text.toString()
            val database =
                FirebaseDatabase.getInstance(Constant.URL).reference.child("Family").child(familyId)
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Toast.makeText(applicationContext, familyId, Toast.LENGTH_LONG).show()
                    if (snapshot.exists()) {
                        Toast.makeText(applicationContext, role, Toast.LENGTH_LONG).show()
                        if (snapshot.child(role).value.toString() == name || snapshot.child("Child")
                                .child(name).exists()
                        ) {
                            if (role == Constant.FATHER || role == Constant.MOTHER) {
                                targetIntent(ParentHomeActivity(), name, familyId, role)
                            } else {
                                targetIntent(ChildHomeActivity(), name, familyId, role)
                            }
                        } else {
                            //if name not exist
                            Toast.makeText(applicationContext, "Name not Exist!", Toast.LENGTH_LONG)
                                .show()
                            val check = addRole(role, name, snapshot)

                            if (check) {
                                if (role == Constant.MOTHER || role == Constant.FATHER) {
                                    targetIntent(ParentHomeActivity(), name, familyId, role)
                                } else {
                                    targetIntent(ChildHomeActivity(), name, familyId, role)
                                }
                            }
                        }
                    } else {
                        Toast.makeText(applicationContext, "FamilyId is wrong!", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
                }

            })
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

    private fun addRole(role: String, name: String, snapshot: DataSnapshot): Boolean {
        if (role == Constant.SON || role == Constant.DAUGHTER) {
            snapshot.ref.child("Child").child(name).child("name").setValue(name)
            snapshot.ref.child("Child").child(name).child("coin").setValue(0)
            return true
        } else {
            if (!snapshot.hasChild(role)) {
                snapshot.ref.child(role).setValue(name)
                return true
            }
        }

        return false
    }

    private fun setupCustomSpinner() {

        val adapter = SpinnerArrayAdapter(this, Roles.list!!)
        binding.role.adapter = adapter
        // TODO Assignment: add listener to the customSpinner
    }

    private fun setupSimpleSpinner() {
        val roles = arrayListOf(Constant.FATHER, Constant.MOTHER, Constant.SON, Constant.DAUGHTER)
        val adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item, roles
        )

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        binding.role.adapter = adapter

        binding.role.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                role = roles[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Use as per your wish
            }
        }
    }
}