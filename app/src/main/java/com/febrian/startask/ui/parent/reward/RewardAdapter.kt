package com.febrian.startask.ui.parent.reward

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.febrian.startask.data.Reward
import com.febrian.startask.databinding.ItemRewardBinding
import com.febrian.startask.databinding.ItemRowParentTaskBinding
import com.febrian.startask.ui.detail.DetailRewardActivity
import com.febrian.startask.utils.Constant

class RewardAdapter(private val reward : ArrayList<Reward>) : RecyclerView.Adapter<RewardAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding : ItemRewardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(r : Reward){
            binding.itemTvTitle.text = r.name
            binding.itemTvAmount.text = r.amount

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailRewardActivity::class.java)
                intent.putExtra(Constant.KEY_REWARD, r.name)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardAdapter.ViewHolder {
        val view = ItemRewardBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RewardAdapter.ViewHolder, position: Int) {
        holder.bind(reward[position])
    }

    override fun getItemCount(): Int {
        return reward.size
    }
}