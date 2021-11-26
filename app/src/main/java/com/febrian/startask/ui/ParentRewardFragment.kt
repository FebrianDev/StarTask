package com.febrian.startask.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.febrian.startask.CreateTaskActivity
import com.febrian.startask.R
import com.febrian.startask.utils.Constant
import io.paperdb.Paper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val familyId = ""
const val role = ""
/**
 * A simple [Fragment] subclass.
 * Use the [ParentRewardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ParentRewardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var tvtaskfamily: TextView
    private lateinit var tvtaskrole: TextView
    private lateinit var btnAddReward: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parent_reward, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvtaskfamily = view.findViewById(R.id.tv_task_familyId)
        tvtaskrole = view.findViewById(R.id.tv_task_role)
        btnAddReward = view.findViewById(R.id.btn_add_reward)
        Paper.init(context)
        val role = Paper.book().read(Constant.ROLE, role)
        val familyId = Paper.book().read(Constant.FAMILY_ID, familyId)
        tvtaskfamily.text = familyId
        tvtaskrole.text = role
        btnAddReward.setOnClickListener{
            //val mIntent = Intent(activity, CreateRewardActivity::class.java)
            //startActivity(mIntent)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ParentRewardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ParentRewardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}