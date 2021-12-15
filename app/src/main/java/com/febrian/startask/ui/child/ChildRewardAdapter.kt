package com.febrian.startask.ui.child

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.febrian.startask.data.Reward
import com.febrian.startask.databinding.ItemRewardBinding

class ChildRewardAdapter(private val reward: ArrayList<Reward>) :
    RecyclerView.Adapter<ChildRewardAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemRewardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(r: Reward) {
            binding.itemTvTitle.text = r.name
            binding.itemTvAmount.text = r.amount

            itemView.setOnClickListener {
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("Redeem Coin")
                builder.setMessage("Are you sure want to redeem coins for this reward?")

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    Toast.makeText(
                        itemView.context,
                        "Yes", Toast.LENGTH_SHORT
                    ).show()

                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                        Toast.makeText(
                            itemView.context,
                            "No", Toast.LENGTH_SHORT
                        ).show()
                    }

                    builder.show()
//
//                    val dbref =
//                        FirebaseDatabase.getInstance(Constant.URL).reference.child("Family")
//                            .child(familyId.toString()).child("Child").child(childName)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChildRewardAdapter.ViewHolder {
        val view = ItemRewardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChildRewardAdapter.ViewHolder, position: Int) {
        holder.bind(reward[position])
    }

    override fun getItemCount(): Int {
        return reward.size
    }
}