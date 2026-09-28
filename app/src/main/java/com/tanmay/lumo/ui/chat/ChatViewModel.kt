package com.tanmay.lumo.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanmay.lumo.data.remote.RetrofitClient
import com.tanmay.lumo.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.tanmay.lumo.data.model.User
import com.tanmay.lumo.data.remote.SocketManager

class ChatViewModel : ViewModel() {

    private val repository = ChatRepository(
        RetrofitClient.api
    )

    private val _uiState =
        MutableStateFlow<ChatUiState>(
            ChatUiState.Idle
        )

    val uiState: StateFlow<ChatUiState> =
        _uiState.asStateFlow()

    private val _conversationState =
        MutableStateFlow<ConversationUiState>(
            ConversationUiState.Idle
        )

    val conversationState: StateFlow<ConversationUiState> =
        _conversationState.asStateFlow()

    private val _onlineUsers =
        MutableStateFlow<Set<String>>(emptySet())

    val onlineUsers: StateFlow<Set<String>> =
        _onlineUsers.asStateFlow()

    init {
        SocketManager.setOnNewMessageListener { message ->

            val currentConversation = _conversationState.value

            if (
                currentConversation is ConversationUiState.Success &&
                message.senderId == currentConversation.selectedUser._id
            ) {

                val alreadyExists = currentConversation.messages.any {
                    it._id == message._id
                }

                if (!alreadyExists) {
                    _conversationState.value = currentConversation.copy(
                        messages = currentConversation.messages + message
                    )
                }

                viewModelScope.launch {
                    try {
                        repository.markMessageAsSeen(message._id)
                    } catch (e: Exception) {
                        // Handle later
                    }
                }

            } else {

                val currentUsersState = _uiState.value

                if (currentUsersState is ChatUiState.Success) {

                    val currentCount =
                        currentUsersState.unseenMessages[message.senderId] ?: 0

                    _uiState.value = currentUsersState.copy(
                        unseenMessages =
                            currentUsersState.unseenMessages +
                                    (message.senderId to currentCount + 1)
                    )
                }
            }
        }

        SocketManager.setOnMessageSeenListener { messageId ->

            val currentState = _conversationState.value

            if (currentState is ConversationUiState.Success) {

                val updatedMessages = currentState.messages.map { message ->

                    if (message._id == messageId) {
                        message.copy(seen = true)
                    } else {
                        message
                    }
                }

                _conversationState.value = currentState.copy(
                    messages = updatedMessages
                )
            }
        }

        SocketManager.setOnOnlineUsersListener { users ->

            _onlineUsers.value = users.toSet()
        }
    }

    fun getUsers() {

        viewModelScope.launch {

            _uiState.value =
                ChatUiState.Loading

            try {

                val response =
                    repository.getUsers()

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body != null) {

                        _uiState.value =
                            ChatUiState.Success(
                                users = body.users,
                                unseenMessages =
                                    body.unseenMessages
                            )

                    } else {

                        _uiState.value =
                            ChatUiState.Error(
                                "Empty response from server"
                            )
                    }

                } else {

                    _uiState.value =
                        ChatUiState.Error(
                            "Failed to load users"
                        )
                }

            } catch (e: Exception) {

                _uiState.value =
                    ChatUiState.Error(
                        e.message
                            ?: "Something went wrong"
                    )
            }
        }
    }

    fun selectUser(user: User) {

        viewModelScope.launch {

            _conversationState.value =
                ConversationUiState.Loading

            try {

                val response =
                    repository.getMessages(user._id)

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body != null) {

                        _conversationState.value =
                            ConversationUiState.Success(
                                selectedUser = user,
                                messages = body.messages,
                                hasMore = body.hasMore,
                                nextCursor = body.nextCursor
                            )

                        val currentUsersState = _uiState.value

                        if (currentUsersState is ChatUiState.Success) {
                            _uiState.value = currentUsersState.copy(
                                unseenMessages =
                                    currentUsersState.unseenMessages - user._id
                            )
                        }

                    } else {

                        _conversationState.value =
                            ConversationUiState.Error(
                                "Empty response from server"
                            )
                    }

                } else {

                    _conversationState.value =
                        ConversationUiState.Error(
                            "Failed to load messages"
                        )
                }

            } catch (e: Exception) {

                _conversationState.value =
                    ConversationUiState.Error(
                        e.message
                            ?: "Something went wrong"
                    )
            }
        }
    }

    fun closeConversation() {
        _conversationState.value =
            ConversationUiState.Idle
    }

    fun sendMessage(text: String) {

        val currentState =
            _conversationState.value

        if (currentState !is ConversationUiState.Success) {
            return
        }

        val trimmedText = text.trim()

        if (trimmedText.isBlank()) {
            return
        }

        viewModelScope.launch {

            try {

                val response =
                    repository.sendMessage(
                        userId = currentState.selectedUser._id,
                        text = trimmedText
                    )

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body != null) {

                        val updatedMessages =
                            currentState.messages +
                                    body.newMessage

                        _conversationState.value =
                            currentState.copy(
                                messages = updatedMessages
                            )
                    }
                }

            } catch (e: Exception) {
                // We'll add proper send-error UI later
            }
        }
    }
}