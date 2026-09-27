package com.tanmay.lumo.data.model

data class Message(
    val _id: String,
    val senderId: String,
    val receiverId: String,
    val text: String? = null,
    val image: String? = null,
    val imagePublicId: String? = null,
    val seen: Boolean = false,
    val edited: Boolean = false,
    val createdAt: String,
    val updatedAt: String
)