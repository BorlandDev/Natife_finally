package com.borlanddev.domain.storage

interface Prefs {

    fun putUsername(username: String)

    fun getUsername(): String
}