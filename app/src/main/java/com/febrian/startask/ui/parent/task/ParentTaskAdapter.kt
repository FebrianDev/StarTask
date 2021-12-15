package com.febrian.startask.ui.parent.task

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.febrian.startask.R
import com.febrian.startask.data.Task
import com.febrian.startask.ui.detail.DetailTaskActivity
import com.febrian.startask.utils.Constant
import com.febrian.startask.utils.TaskTitleView

class ParentTaskAdapter(private val familyList: ArrayList<Task>, private val myName: ArrayList<String>) :
    RecyclerView.Adapter<ParentTaskAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = familyList[position]

        holder.bind(currentItem, myName[position])
    }

    override fun getItemCount(): Int {
        return familyList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TaskTitleView = itemView.findViewById(R.id.item_tv_title)
        val amount: TextView = itemView.findViewById(R.id.item_tv_amount)
        val checkBox : CheckBox = itemView.findViewById(R.id.item_checkbox)

        lateinit var getTask: String
        lateinit var childName: String

        fun bind(currentItem: Task, myName: String) {
            checkBox.isClickable = false
            var condition = 0
            if (currentItem.complete == "1") {
                condition = TaskTitleView.DONE
                checkBox.isChecked = true
            }
            else {
               condition = TaskTitleView.NORMAL
                checkBox.isChecked = false
            }

            getTask = currentItem.name.toString()
            childName = myName

            name.state = condition
            name.text = currentItem.name
            amount.text = currentItem.amount

            itemView.setOnClickListener {
                if(currentItem.complete == "0") {
                    val intent = Intent(itemView.context, DetailTaskActivity::class.java)
                    intent.putExtra(Constant.KEY_TASK, currentItem.name)
                    intent.putExtra(Constant.KEY_NAME, myName)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}