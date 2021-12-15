package com.febrian.startask.data

import com.google.firebase.database.PropertyName

data class Child(
    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String? = null,
    @get:PropertyName("task")
    @set:PropertyName("task")
    var task: Task? = null,

    var coin: Int? = null
)
