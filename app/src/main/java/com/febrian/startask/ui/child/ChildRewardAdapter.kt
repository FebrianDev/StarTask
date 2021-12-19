package com.febrian.startask.ui.child

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.febrian.startask.data.Reward
import com.febrian.startask.databinding.ItemRewardBinding
import com.febrian.startask.utils.Constant
import com.febrian.startask.utils.CreateFamilyId
import com.febrian.startask.utils.Rand
import com.febrian.startask.utils.TaskTitleView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random

class ChildRewardAdapter(
    private val reward: ArrayList<Reward>,
    private val coin: Int,
    private val familyId: String,
    private val childName: String
) :
    RecyclerView.Adapter<ChildRewardAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemRewardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(r: Reward) {

            binding.itemTvTitle.text = r.name
            binding.itemTvAmount.text = r.amount

            itemView.setOnClickListener {

                if (r.amount?.toInt()!! <= coin) {
                    val rand =  Random.nextInt(0, 1000)
                    val builder = AlertDialog.Builder(itemView.context)
                    builder.setTitle("Redeem Coin")
                    builder.setMessage("Are you sure want to redeem coins for this reward?")

                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        Toast.makeText(
                            itemView.context,
                            "Yes", Toast.LENGTH_SHORT
                        ).show()

                        val dbref =
                            FirebaseDatabase.getInstance(Constant.URL).reference.child("Family")
                                .child(familyId.toString())
                        val tempCoin = (coin - r.amount!!.toInt())
                        dbref.child("Child").child(childName).child("coin").setValue(tempCoin)

                        dbref.child("Reward").child(r.name.toString()).child("name").get()
                            .addOnSuccessListener {
                                dbref.child("Child").child(childName).child("Reward")
                                    .child("${it.value.toString()} $rand")
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            snapshot.ref.child("id").setValue("${it.value.toString()} $rand")
                                            snapshot.ref.child("name").setValue(it.value.toString())
                                            snapshot.ref.child("complete").setValue(false)
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Toast.makeText(
                                                itemView.context,
                                                error.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    })
                            }
                    }

                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                        Toast.makeText(
                            itemView.context,
                            "No", Toast.LENGTH_SHORT
                        ).show()
                    }

                    builder.show()

                } else {
                    Toast.makeText(
                        itemView.context,
                        "Coin not enough!", Toast.LENGTH_SHORT
                    ).show()
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