package com.tanmay.lumo.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tanmay.lumo.data.model.Message
import com.tanmay.lumo.data.model.User
import androidx.compose.runtime.*

@Composable
fun ConversationScreen(
    state: ConversationUiState,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    onlineUsers: Set<String>
) {

    when (state) {

        ConversationUiState.Idle -> Unit

        ConversationUiState.Loading -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ConversationUiState.Error -> {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    onClick = onBack
                ) {
                    Text("Back")
                }
            }
        }

        is ConversationUiState.Success -> {

            ConversationContent(
                user = state.selectedUser,
                messages = state.messages,
                onBack = onBack,
                onSendMessage = onSendMessage,
                isOnline = state.selectedUser._id in onlineUsers
            )
        }
    }
}

@Composable
private fun ConversationContent(
    user: User,
    messages: List<Message>,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    isOnline: Boolean
) {

    var messageText by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                onClick = onBack
            ) {
                Text("Back")
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Column {
                Text(
                    text = user.fullName
                )

                Text(
                    text = if (isOnline) "Online" else "Offline",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        if (messages.isEmpty()) {

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("No messages yet")
            }

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                items(
                    items = messages,
                    key = { message -> message._id }
                ) { message ->

                    MessageItem(
                        message = message,
                        selectedUser = user
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                value = messageText,
                onValueChange = {
                    messageText = it
                },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text("Type a message...")
                },
                singleLine = true
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Button(
                onClick = {

                    if (messageText.isNotBlank()) {

                        onSendMessage(messageText)

                        messageText = ""
                    }
                }
            ) {
                Text("Send")
            }
        }


    }
}

@Composable
private fun MessageItem(
    message: Message,
    selectedUser: User
) {

    val receivedMessage =
        message.senderId == selectedUser._id

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            if (receivedMessage) {
                Arrangement.Start
            } else {
                Arrangement.End
            }
    ) {

        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 2.dp
        ) {

            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    )
                    .widthIn(max = 280.dp)
            ) {

                if (!message.text.isNullOrBlank()) {
                    Text(
                        text = message.text
                    )
                }

                if (!message.image.isNullOrBlank()) {

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "[Image]"
                    )
                }

                if (message.edited) {

                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    Text(
                        text = "edited",
                        style =
                            MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}