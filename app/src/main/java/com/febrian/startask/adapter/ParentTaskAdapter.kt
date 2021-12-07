package com.febrian.startask.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.febrian.startask.R
import com.febrian.startask.data.Child
import com.febrian.startask.data.Parent
import com.febrian.startask.data.Task
import com.febrian.startask.ui.detail.DetailTaskActivity
import com.febrian.startask.utils.Constant

class ParentTaskAdapter(private val familyList: ArrayList<Task>, private val name : String): RecyclerView.Adapter<ParentTaskAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = familyList[position]

        holder.name.text = currentItem.name
        holder.amount.text = currentItem.amount

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailTaskActivity::class.java)
            intent.putExtra(Constant.KEY_TASK, currentItem.name)
            intent.putExtra(Constant.KEY_NAME, name)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return familyList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.item_tv_title)
        val amount: TextView = itemView.findViewById(R.id.item_tv_amount)
    }
}