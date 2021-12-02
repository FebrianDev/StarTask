package com.febrian.startask.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.febrian.startask.R
import com.febrian.startask.data.Parent
import com.febrian.startask.data.Task

class ParentTaskAdapter(private val familyList: ArrayList<Task>): RecyclerView.Adapter<ParentTaskAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_row_parent_task, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = familyList[position]

        holder.name.text = currentitem.name
        holder.amount.text = currentitem.amount
    }

    override fun getItemCount(): Int {
        return familyList.size
    }


    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.tv_item_task)
        val amount: TextView = itemView.findViewById(R.id.tv_item_amount)


    }
}