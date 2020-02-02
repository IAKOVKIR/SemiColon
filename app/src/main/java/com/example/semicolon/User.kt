package com.example.semicolon

/**
 * A User item representing a piece of pos.
 */
class User(val id: Int, val username: String, val firstName: String, val lastName: String, val phone : String, val password : String, val rating : Float, val email: String) {
    constructor(): this(-1, "-1", "-1", "-1", "-1", "-1", -1.0F, "-1")
    constructor(username: String, firstName: String, lastName: String, phone : String, password : String, rating : Float, email: String): this(-1, username, firstName, lastName, phone, password, rating, email)
    constructor(username: String, password: String, email: String): this(-1, username, "-1", "-1", "-1", password, 5.0F, email)
}