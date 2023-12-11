package com.example.snapguard.models

data class User(
    val userId: String,         // User ID
    val email: String, // emailId
    val password: String,       // Password
    val name: String, // name
    val phoneNumber: String,    // Phone Number
    val profilePhotoUrl: String, // Profile Photo URL
) {
    // Constructor with only name and password
}
