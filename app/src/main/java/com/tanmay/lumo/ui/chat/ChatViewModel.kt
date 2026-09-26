package com.tanmay.lumo.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanmay.lumo.data.remote.RetrofitClient
import com.tanmay.lumo.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
}