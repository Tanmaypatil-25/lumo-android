package com.tanmay.lumo.data.model

data class AuthResponse(
    val success: Boolean,
    val userData: User,
    val token: String,
    val message: String
)