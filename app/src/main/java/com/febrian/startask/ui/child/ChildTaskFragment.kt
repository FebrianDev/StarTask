package com.febrian.startask.ui.child

import android.content.Context
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
import com.febrian.startask.data.Task
import com.febrian.startask.databinding.FragmentChildTaskBinding
import com.febrian.startask.ui.parent.task.ParentTaskAdapter
import com.febrian.startask.utils.Constant
import com.google.firebase.database.*

class ChildTaskFragment : Fragment() {

    private lateinit var dbref: DatabaseReference
    private lateinit var childTaskRecyclerView: RecyclerView
    private lateinit var childTaskArrayList: ArrayList<Task>

    private var _binding: FragmentChildTaskBinding? = null

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

        _binding = FragmentChildTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = this.requireActivity()
            .getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        familyId = preferences.getString(Constant.FAMILY_ID, "").toString()
        childName = preferences.getString(Constant.KEY_NAME, "").toString()

        childTaskRecyclerView = binding.rvShowAllTask
        childTaskRecyclerView.layoutManager = LinearLayoutManager(context)
        childTaskRecyclerView.setHasFixedSize(true)

        childTaskArrayList = ArrayList()

        dbref = FirebaseDatabase.getInstance(Constant.URL).reference.child("Family")
            .child(familyId.toString()).child("Child").child(childName)

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
                    ChildTaskAdapter(childTaskArrayList, familyId.toString(), childName)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun showActive() {
        childTaskArrayList.clear()
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.coin.text = "Coin " + snapshot.child("coin").value.toString()
                for (taskFamily in snapshot.child("task").children) {
                    val all = taskFamily.getValue(Task::class.java)
                    if (all!!.complete == "0")
                        childTaskArrayList.add(all)
                    Log.d("Test", all.complete.toString())

                }

                childTaskRecyclerView.adapter =
                    ChildTaskAdapter(childTaskArrayList, familyId.toString(), childName)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun showComplete() {
        childTaskArrayList.clear()
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.coin.text = "Coin " + snapshot.child("coin").value.toString()
                for (taskFamily in snapshot.child("task").children) {
                    val all = taskFamily.getValue(Task::class.java)
                    if (all!!.complete == "1") {
                        childTaskArrayList.add(all)
                        Log.d("Test", all.complete.toString())
                    }

                }

                childTaskRecyclerView.adapter =
                    ChildTaskAdapter(childTaskArrayList, familyId.toString(), childName)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun showAll() {
        childTaskArrayList.clear()
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.coin.text = "Coin " + snapshot.child("coin").value.toString()
                for (taskFamily in snapshot.child("task").children) {
                    val all = taskFamily.getValue(Task::class.java)
                    childTaskArrayList.add(all!!)
                    Log.d("Test", all.complete.toString())
                }

                childTaskRecyclerView.adapter =
                    ChildTaskAdapter(childTaskArrayList, familyId.toString(), childName)

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

    override fun onResume() {
        super.onResume()
        showAll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}