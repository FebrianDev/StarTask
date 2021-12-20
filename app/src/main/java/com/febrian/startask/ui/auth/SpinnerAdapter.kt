package com.febrian.startask.ui.auth

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.febrian.startask.R
import com.febrian.startask.utils.Role
import kotlinx.android.synthetic.main.spinner_item.view.*

class SpinnerArrayAdapter(context: Context, roleList: List<Role>) : ArrayAdapter<Role>(context, 0, roleList) {

    override fun getView(position: Int, roleView: View?, parent: ViewGroup): View {
        return initView(position, roleView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {

        val role = getItem(position)

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        view.roleImage.setImageResource(role!!.image)
        view.roleName.text = role.name

        return view
    }
}