package com.tanmay.lumo.ui.chat

import com.tanmay.lumo.data.model.Message
import com.tanmay.lumo.data.model.User

sealed class ConversationUiState {

    data object Idle : ConversationUiState()

    data object Loading : ConversationUiState()

    data class Success(
        val selectedUser: User,
        val messages: List<Message>,
        val hasMore: Boolean,
        val nextCursor: String? = null
    ) : ConversationUiState()

    data class Error(
        val message: String
    ) : ConversationUiState()
}