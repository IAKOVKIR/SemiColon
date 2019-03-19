package com.example.semicolon

/**
 * An Event item representing a piece of pos.
 */
data class User(val id: String, val firstName: String, val lastName: String, val city : String, val phoneNum : String) {
    override fun toString(): String = "$id $firstName $lastName $city $phoneNum"
}