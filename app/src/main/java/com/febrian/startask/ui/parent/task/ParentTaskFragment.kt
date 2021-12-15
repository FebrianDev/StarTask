package com.febrian.startask.ui.parent.task

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.febrian.startask.R
import com.febrian.startask.data.Child
import com.febrian.startask.data.Task
import com.febrian.startask.databinding.FragmentParentTaskBinding
import com.febrian.startask.utils.Constant
import com.google.firebase.database.*

class ParentTaskFragment : Fragment() {

    private lateinit var dbref: DatabaseReference
    private lateinit var parentTaskRecyclerView: RecyclerView
    private lateinit var parentTaskArrayList: ArrayList<Task>
    private lateinit var listChildName: ArrayList<String>

    private var _binding: FragmentParentTaskBinding? = null

    private lateinit var preferences: SharedPreferences
    private lateinit var familyId: String
    private lateinit var childName: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentParentTaskBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = this.requireActivity()
            .getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        familyId = preferences.getString(Constant.FAMILY_ID, "").toString()

        parentTaskRecyclerView = binding.rvShowAllTask
        parentTaskRecyclerView.layoutManager = LinearLayoutManager(context)
        parentTaskRecyclerView.setHasFixedSize(true)

        parentTaskArrayList = ArrayList()
        listChildName = ArrayList()

        initAction(familyId)
        dbref = FirebaseDatabase.getInstance(Constant.URL).reference.child("Family")
            .child(familyId.toString()).child("Child")

        binding.btnAddTask.setOnClickListener {
            val mIntent = Intent(activity, CreateTaskActivity::class.java)
            startActivity(mIntent)
        }

    }

    private fun showActive() {
        parentTaskArrayList.clear()
        listChildName.clear()
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (familySnapshot in snapshot.children) {
                    val family = familySnapshot.getValue(Child::class.java)!!
                    for (taskFamily in familySnapshot.child("task").children) {
                        val all = taskFamily.getValue(Task::class.java)
                        if (all!!.complete == "0")
                            parentTaskArrayList.add(all)

                        listChildName.add(family.name.toString())
                        Log.d("Test", all.complete.toString())
                    }

                    childName = family.name.toString()
                }

                parentTaskRecyclerView.adapter =
                    ParentTaskAdapter(parentTaskArrayList, listChildName)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun showComplete() {
        listChildName.clear()
        parentTaskArrayList.clear()
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (familySnapshot in snapshot.children) {
                    val family = familySnapshot.getValue(Child::class.java)!!
                    for (taskFamily in familySnapshot.child("task").children) {
                        val all = taskFamily.getValue(Task::class.java)
                        if (all!!.complete == "1") {
                            parentTaskArrayList.add(all)

                            listChildName.add(family.name.toString())
                            Log.d("Test", all!!.complete.toString())
                        }
                    }

                    childName = family.name.toString()
                }

                parentTaskRecyclerView.adapter =
                    ParentTaskAdapter(parentTaskArrayList, listChildName)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun showAll() {
        listChildName.clear()
        parentTaskArrayList.clear()
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (familySnapshot in snapshot.children) {
                    val family = familySnapshot.getValue(Child::class.java)!!
                    for (taskFamily in familySnapshot.child("task").children) {
                        val all = taskFamily.getValue(Task::class.java)
                        parentTaskArrayList.add(all!!)

                        listChildName.add(family.name.toString())
                        Log.d("Test", all.complete.toString())
                    }

                    childName = family.name.toString()
                }

                parentTaskRecyclerView.adapter =
                    ParentTaskAdapter(parentTaskArrayList, listChildName)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                showFilteringPopUpMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showFilteringPopUpMenu() {
        val view = requireActivity().findViewById<View>(R.id.action_filter)
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.filter_tasks, menu)

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.active -> {
                        Log.d("Test", "Active")
                        showActive()
                    }
                    R.id.completed -> {
                        Log.d("Test", "Complete")
                        showComplete()
                    }
                    else -> {
                        Log.d("Test", "All")
                        showAll()
                    }
                }
                true
            }
            show()
        }
    }

    private fun initAction(familyId: String) {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = (viewHolder as ParentTaskAdapter.MyViewHolder).getTask
                val myName = (viewHolder as ParentTaskAdapter.MyViewHolder).childName
                val reference = FirebaseDatabase.getInstance(Constant.URL).reference.child("Family")
                    .child(familyId.toString()).child("Child").child(myName).child("task")
                reference.child(task).removeValue()
                Log.d("Name", task)
                Toast.makeText(requireContext(), task, Toast.LENGTH_LONG).show()
            }

        })
        itemTouchHelper.attachToRecyclerView(parentTaskRecyclerView)
    }

    override fun onResume() {
        super.onResume()
        parentTaskArrayList.clear()
        listChildName.clear()
        showAll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}