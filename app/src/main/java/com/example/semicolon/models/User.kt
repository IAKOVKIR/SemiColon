package com.example.semicolon.models

/**
 * A User item representing a piece of pos.
 */
data class User(val userId: Int, val username: String, val phone : String, val password : String, val fullName: String, val bioDescription : String, val email: String, val rating : Float, val lastModified : String, val dateCreated : String) {
    constructor(): this(-1, "-1", "-1", "-1", "-1", "-1", "-1", -1.0F,"-1","-1")
    constructor(username: String, password : String, email: String): this (-1, username, "-1", password, "-1", "-1", email, 5.0F, "-1", "-1")
    constructor(username: String, phone : String, password : String, fullName: String, bioDescription : String, email: String, rating : Float, lastModified : String, dateCreated : String): this(-1, username, phone, password, fullName, bioDescription, email, rating, lastModified, dateCreated)
}