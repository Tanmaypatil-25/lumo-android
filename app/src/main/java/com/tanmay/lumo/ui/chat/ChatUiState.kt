package com.tanmay.lumo.ui.chat

import com.tanmay.lumo.data.model.User

sealed class ChatUiState {

    data object Idle : ChatUiState()

    data object Loading : ChatUiState()

    data class Success(
        val users: List<User>,
        val unseenMessages: Map<String, Int>
    ) : ChatUiState()

    data class Error(
        val message: String
    ) : ChatUiState()
}