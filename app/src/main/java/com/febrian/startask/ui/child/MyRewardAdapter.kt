package com.febrian.startask.ui.child

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.febrian.startask.data.MyReward
import com.febrian.startask.data.Task
import com.febrian.startask.databinding.ItemTaskBinding
import com.febrian.startask.utils.Constant
import com.febrian.startask.utils.TaskTitleView
import com.google.firebase.database.FirebaseDatabase

class MyRewardAdapter(private val list: ArrayList<MyReward>,
                      private val familyId: String,
                      private val childName: String) :
    RecyclerView.Adapter<MyRewardAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding : ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){

        lateinit var myReward : MyReward

        fun bind(r : MyReward){

            myReward = r

            var condition = 0
            if (r.complete == true) {
                binding.itemCheckbox.isChecked = true
                condition = TaskTitleView.DONE
            }
            else {
                binding.itemCheckbox.isChecked = false
                condition = TaskTitleView.NORMAL
            }
            binding.itemTvTitle.state = condition

            binding.itemCheckbox.isClickable = false
            binding.itemTvTitle.text = r.name.toString()

            itemView.setOnClickListener {
                if(r.complete == false){
                    val builder = AlertDialog.Builder(itemView.context)
                    builder.setTitle("Reward has been given?")

                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        Toast.makeText(
                            itemView.context,
                            "Yes", Toast.LENGTH_SHORT
                        ).show()

                        val dbref = FirebaseDatabase.getInstance(Constant.URL).reference.child("Family")
                            .child(familyId.toString()).child("Child").child(childName).child("Reward")
                        dbref.child(r.id.toString()).child("complete").setValue(true)

                        r.complete = true
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRewardAdapter.ViewHolder {
        val view = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRewardAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}