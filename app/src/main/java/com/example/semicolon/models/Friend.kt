package com.example.semicolon.models

data class Friend(val id : Int, val SenderID : Int, val ReceiverID : Int, val date : String, val time : String, val condition : Int) {
    constructor(senderId: Int, receiverId: Int, date: String, time: String, condition: Int): this(-1, senderId, receiverId, date, time, condition)
}