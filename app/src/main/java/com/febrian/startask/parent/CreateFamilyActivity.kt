package com.febrian.startask.parent

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.febrian.startask.databinding.ActivityCreateFamilyBinding
import com.febrian.startask.utils.Constant
import com.febrian.startask.utils.CreateFamilyId
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CreateFamilyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateFamilyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateFamilyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val role = intent.getStringExtra(Constant.ROLE)

        binding.create.setOnClickListener {
            val name = binding.name.text.toString()
            val key = getKey(role.toString())
            val familyId = CreateFamilyId.get()

            val database =
                FirebaseDatabase.getInstance("https://startask-6ff75-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child(
                    "Family"
                ).child(familyId)

            if (name.isEmpty()) {
                binding.name.error = "Name must be filled"
            } else {
                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.child("familyId").setValue(familyId)
                        snapshot.ref.child(key).setValue(name)

                        //goto parent home
                       // targetIntent()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
                    }

                })
            }
        }
    }

    private fun getKey(role: String): String {
        return when (role) {
            Constant.FATHER -> {
                "father"
            }
            Constant.MOTHER -> {
                "mother"
            }
            else -> ""
        }
    }

    private fun targetIntent(activity: AppCompatActivity, id: String) {
        val intent = Intent(applicationContext, activity::class.java)
        intent.putExtra(Constant.FAMILY_ID, id)
        startActivity(intent)
    }
}