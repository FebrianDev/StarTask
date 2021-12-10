package com.febrian.startask.ui.child

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.febrian.startask.adapter.ParentTaskAdapter
import com.febrian.startask.data.Child
import com.febrian.startask.data.Task
import com.febrian.startask.databinding.FragmentChildTaskBinding
import com.febrian.startask.utils.Constant
import com.google.firebase.database.*

class ChildTaskFragment : Fragment() {

    private lateinit var dbref: DatabaseReference
    private lateinit var childTaskRecyclerView: RecyclerView
    private lateinit var childTaskArrayList: ArrayList<Task>

    private var _binding: FragmentChildTaskBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChildTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getChildTaskData()
    }

    private fun getChildTaskData() { //next merubah ke viewModel
        val preferences = this.requireActivity()
            .getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        val familyId: String? = preferences.getString(Constant.FAMILY_ID, "")
        val name = preferences.getString(Constant.KEY_NAME, "")

        childTaskRecyclerView = binding.rvShowAllTask
        childTaskRecyclerView.layoutManager = LinearLayoutManager(context)
        childTaskRecyclerView.setHasFixedSize(true)

        childTaskArrayList = ArrayList()

        dbref = FirebaseDatabase.getInstance(Constant.URL).reference.child("Family")
            .child(familyId.toString()).child("Child").child(name.toString())
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                    for (taskFamily in snapshot.child("task").children) {
                        val all = taskFamily.getValue(Task::class.java)
                        childTaskArrayList.add(all!!)
                    }

                childTaskRecyclerView.adapter =
                    ChildTaskAdapter(childTaskArrayList)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}