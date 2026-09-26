package com.tanmay.lumo.data.model

data class SignupRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val bio: String
)