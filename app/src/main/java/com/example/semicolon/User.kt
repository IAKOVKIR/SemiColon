package com.example.semicolon

/**
 * A User item representing a piece of pos.
 */
data class User(val id: Int, val firstName: String, val lastName: String, val phone : String, val password : String, val city : String, val agreementCheck : Byte, val rating : Float, val email: String)