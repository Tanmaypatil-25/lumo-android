package com.tanmay.lumo.data.model

data class User(
    val _id: String,
    val email: String,
    val fullName: String,
    val profilePic: String = "",
    val bio: String? = null,
    val createdAt: String,
    val updatedAt: String
)