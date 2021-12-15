package com.febrian.startask.ui.child

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.febrian.startask.data.Task
import com.febrian.startask.databinding.ItemTaskBinding
import com.febrian.startask.utils.Constant
import com.febrian.startask.utils.TaskTitleView
import com.google.firebase.database.FirebaseDatabase

class ChildTaskAdapter(
    private val list: ArrayList<Task>,
    private val familyId: String,
    private val childName: String
) :
    RecyclerView.Adapter<ChildTaskAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {

            var condition = 0
             if (task.complete == "1") {
                 binding.itemCheckbox.isChecked = true
                 condition = TaskTitleView.DONE
             }
            else {
                 binding.itemCheckbox.isChecked = false
                 condition = TaskTitleView.NORMAL
             }
            binding.itemTvTitle.state = condition
            binding.itemTvTitle.text = task.name
            binding.itemTvAmount.text = task.amount
            binding.itemCheckbox.isClickable = false

            itemView.setOnClickListener {

                if (task.complete == "0") {
                    val builder = AlertDialog.Builder(itemView.context)
                    builder.setTitle("Already Finished Task?")

                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        Toast.makeText(
                            itemView.context,
                            "Yes", Toast.LENGTH_SHORT
                        ).show()

                        val dbref =
                            FirebaseDatabase.getInstance(Constant.URL).reference.child("Family")
                                .child(familyId.toString()).child("Child").child(childName)

                        dbref.child("task")
                            .child(task.name.toString())
                            .child("complete")
                            .setValue("1")

                        var amount : Int? = null
                        dbref.child("task")
                            .child(task.name.toString()).child("amount").get()
                            .addOnSuccessListener {
                                amount = it.value.toString().toInt()
                                Log.d("Resilt", amount.toString())

                                var tempAmount : Int? = null
                                dbref.child("coin").get().addOnSuccessListener {
                                    tempAmount = it.value.toString().toInt()
                                    Log.d("Resilt", tempAmount.toString())

                                    val result : Int = amount!! + tempAmount!!

                                    Log.d("Resilt", result.toString())

                                    dbref.child("coin").setValue(result)

                                    binding.itemTvAmount.text = result.toString()
                                }
                            }

                        task.complete = "1"
                        binding.itemCheckbox.isChecked = true
                        binding.itemTvTitle.state = TaskTitleView.DONE
                    }

                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                        Toast.makeText(
                            itemView.context,
                            "No", Toast.LENGTH_SHORT
                        ).show()
                    }

                    builder.show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildTaskAdapter.ViewHolder {
        val view = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChildTaskAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}