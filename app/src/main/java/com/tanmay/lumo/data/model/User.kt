package com.tanmay.lumo.data.model

data class User(
    val _id: String,
    val email: String? = null,
    val fullName: String,
    val profilePic: String = "",
    val bio: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)