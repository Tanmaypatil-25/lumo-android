package com.tanmay.lumo.ui.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tanmay.lumo.data.model.User

@Composable
fun HomeScreen(
    chatViewModel: ChatViewModel,
    onLogout: () -> Unit
) {

    val uiState by chatViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        chatViewModel.getUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Lumo",
                style = MaterialTheme.typography.headlineMedium
            )

            TextButton(
                onClick = onLogout
            ) {
                Text("Logout")
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        when (val state = uiState) {

            ChatUiState.Idle,
            ChatUiState.Loading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ChatUiState.Error -> {

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
                        modifier = Modifier.height(12.dp)
                    )

                    Button(
                        onClick = {
                            chatViewModel.getUsers()
                        }
                    ) {
                        Text("Retry")
                    }
                }
            }

            is ChatUiState.Success -> {

                LazyColumn(
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    items(
                        items = state.users,
                        key = { user -> user._id }
                    ) { user ->

                        UserItem(
                            user = user,
                            unseenCount =
                                state.unseenMessages[
                                    user._id
                                ] ?: 0
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserItem(
    user: User,
    unseenCount: Int
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Conversation navigation comes next
            }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = user.fullName,
                    style =
                        MaterialTheme.typography.titleMedium
                )

                if (!user.bio.isNullOrBlank()) {

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = user.bio,
                        style =
                            MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (unseenCount > 0) {

                Badge {
                    Text(
                        text = unseenCount.toString()
                    )
                }
            }
        }
    }
}