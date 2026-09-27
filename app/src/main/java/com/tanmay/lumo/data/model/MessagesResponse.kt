package com.tanmay.lumo.data.model

data class MessagesResponse(
    val success: Boolean,
    val messages: List<Message>,
    val hasMore: Boolean,
    val nextCursor: String? = null
)