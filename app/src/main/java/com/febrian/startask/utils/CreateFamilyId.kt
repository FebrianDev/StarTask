package com.febrian.startask.utils

object CreateFamilyId {
    fun get() : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..7)
            .map { allowedChars.random() }
            .joinToString("")
    }
}