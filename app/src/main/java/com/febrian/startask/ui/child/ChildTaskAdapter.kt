package com.febrian.startask.ui.child

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.febrian.startask.data.Task
import com.febrian.startask.databinding.ItemTaskBinding

class ChildTaskAdapter(private val list : ArrayList<Task>) :
    RecyclerView.Adapter<ChildTaskAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(task : Task){
            binding.itemTvTitle.text = task.name
            binding.itemTvAmount.text = task.amount
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