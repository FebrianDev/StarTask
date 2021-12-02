package com.febrian.startask.ui
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.febrian.startask.CreateTaskActivity
import com.febrian.startask.adapter.ParentTaskAdapter
import com.febrian.startask.data.Parent
import com.febrian.startask.data.Task
import com.febrian.startask.databinding.FragmentParentTaskBinding
import com.febrian.startask.utils.Constant
import com.google.firebase.database.*

class ParentTaskFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var parentTaskRecyclerView: RecyclerView
    private lateinit var parentTaskArrayList: ArrayList<Task>

    private var _binding: FragmentParentTaskBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentParentTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getParentTaskData()

            binding.btnAddTask.setOnClickListener{
            val mIntent = Intent(activity, CreateTaskActivity::class.java)
            startActivity(mIntent)
        }

        return root
    }

    private fun getParentTaskData() { //next merubah ke viewModel
        val preferences = this.requireActivity().getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        val role: String? = preferences.getString(Constant.ROLE, null)
        val familyId: String? = preferences.getString(Constant.FAMILY_ID, null)

        parentTaskRecyclerView = binding.rvShowAllTask
        parentTaskRecyclerView.layoutManager = LinearLayoutManager(context)
        parentTaskRecyclerView.setHasFixedSize(true)

        parentTaskArrayList = arrayListOf<Task>()

        dbref = FirebaseDatabase.getInstance(Constant.URL).reference.child("Family").child(familyId.toString()).child("Child").child("f").child("task")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(familySnapshot in snapshot.children){
                        val family = familySnapshot.getValue(Task::class.java)
                        parentTaskArrayList.add(family!!)
                    }
                    parentTaskRecyclerView.adapter = ParentTaskAdapter(parentTaskArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}