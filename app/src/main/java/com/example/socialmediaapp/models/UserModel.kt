package com.example.socialmediaapp.models
@Suppress("unused")
class UserModel(val name: String, val profession: String, val email: String, val coverPhoto: String, val profilePhoto: String, val followers: String,
    val friends: String, val posts: String, val about: String) {

    constructor() : this("", "", "", "", "", "0", "0", "0", "")
}