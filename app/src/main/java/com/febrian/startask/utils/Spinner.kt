package com.febrian.startask.utils

import com.febrian.startask.R

data class Role(val image: Int, val name: String)

object Roles {

    private val images = intArrayOf(
        R.drawable.father,
        R.drawable.mother,
        R.drawable.son,
        R.drawable.daughter,
    )

    private val roles = arrayOf(
        "FATHER",
        "MOTHER",
        "SON",
        "DAUGHTER",
    )

    var list: ArrayList<Role>? = null
        get() {

            if (field != null)
                return field

            field = ArrayList()
            for (i in images.indices) {

                val imageId = images[i]
                val roleName = roles[i]

                val role = Role(imageId, roleName)
                field!!.add(role)
            }

            return field
        }
}