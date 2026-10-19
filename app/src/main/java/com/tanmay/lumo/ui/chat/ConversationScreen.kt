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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import coil3.compose.AsyncImage
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Box
import android.content.Context
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi


@Composable
fun ConversationScreen(
    state: ConversationUiState,
    onBack: () -> Unit,
    onSendMessage: (String, MultipartBody.Part?) -> Unit,
    onlineUsers: Set<String>,
    typingUsers: Set<String>,
    onTyping: (String) -> Unit,
    onStopTyping: (String) -> Unit,
    onEditMessage: (String, String) -> Unit,
    onDeleteMessage: (String) -> Unit,
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
                onEditMessage = onEditMessage,
                onDeleteMessage = onDeleteMessage,
                isOnline = state.selectedUser._id in onlineUsers,
                isTyping = state.selectedUser._id in typingUsers,
                onTyping = onTyping,
                onStopTyping = onStopTyping
            )
        }
    }
}

@Composable
private fun ConversationContent(
    user: User,
    messages: List<Message>,
    onBack: () -> Unit,
    onSendMessage: (String, MultipartBody.Part?) -> Unit,
    onEditMessage: (String, String) -> Unit,
    onDeleteMessage: (String) -> Unit,
    isOnline: Boolean,
    isTyping: Boolean,
    onTyping: (String) -> Unit,
    onStopTyping: (String) -> Unit
) {

    var messageText by remember {
        mutableStateOf("")
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var editingMessageId by remember {
        mutableStateOf<String?>(null)
    }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->

            if (uri != null) {
                selectedImageUri = uri
            }
        }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    var typingJob by remember {
        mutableStateOf<Job?>(null)
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
                onClick = {
                    typingJob?.cancel()
                    onStopTyping(user._id)
                    onBack()
                }
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
                    text = when {
                        isTyping -> "Typing..."
                        isOnline -> "Online"
                        else -> "Offline"
                    },
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
                        selectedUser = user,
                        onEdit = {
                            typingJob?.cancel()
                            onStopTyping(user._id)

                            selectedImageUri = null
                            editingMessageId = message._id
                            messageText = message.text.orEmpty()
                        },
                        onDelete = {
                            if (editingMessageId == message._id) {
                                editingMessageId = null
                                messageText = ""
                            }
                            onDeleteMessage(message._id)
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        if (selectedImageUri != null) {

            Box(
                modifier = Modifier.size(110.dp)
            ) {

                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Selected image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                TextButton(
                    onClick = {
                        selectedImageUri = null
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text("X")
                }
            }
        }

        if (editingMessageId != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Editing message")

                TextButton(
                    onClick = {
                        editingMessageId = null
                        messageText = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                onClick = {
                    imagePickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                enabled = editingMessageId == null
            ) {
                Text("Image")
            }

            OutlinedTextField(
                value = messageText,
                onValueChange = { newValue ->

                    messageText = newValue

                    // Cancel the previous stop-typing timer
                    typingJob?.cancel()

                    if (newValue.isNotBlank()) {

                        onTyping(user._id)

                        // If there are no more keystrokes for 1 second,
                        // consider the user to have stopped typing
                        typingJob = scope.launch {
                            delay(1000)
                            onStopTyping(user._id)
                        }

                    } else {
                        onStopTyping(user._id)
                    }
                },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        if (editingMessageId != null) {
                            "Edit message..."
                        } else {
                            "Type a message..."
                        }
                    )
                },
                singleLine = true
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Button(
                onClick = {
                    val messageId = editingMessageId

                    if (messageId != null) {

                        if (messageText.isNotBlank()) {
                            onEditMessage(
                                messageId,
                                messageText.trim()
                            )

                            editingMessageId = null
                            messageText = ""
                        }

                    } else if (
                        messageText.isNotBlank() ||
                        selectedImageUri != null
                    ) {

                        typingJob?.cancel()
                        onStopTyping(user._id)

                        val imagePart = selectedImageUri?.let { uri ->
                            uriToMultipart(
                                context = context,
                                uri = uri
                            )
                        }

                        onSendMessage(
                            messageText,
                            imagePart
                        )

                        messageText = ""
                        selectedImageUri = null
                    }
                }
            ) {
                Text(
                    if (editingMessageId != null) "Save" else "Send"
                )
            }
        }


    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MessageItem(
    message: Message,
    selectedUser: User,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    val receivedMessage = message.senderId == selectedUser._id

    var showMenu by remember {
        mutableStateOf(false)
    }

    val canDelete = !receivedMessage
    val canEdit = !receivedMessage && !message.text.isNullOrBlank()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            if (receivedMessage) {
                Arrangement.Start
            } else {
                Arrangement.End
            }
    ) {

        Box {

            Surface(
                modifier = Modifier.combinedClickable(
                    onClick = {},
                    onLongClick = {
                        if (canDelete) {
                            showMenu = true
                        }
                    }
                ),
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

                    if (!message.image.isNullOrBlank()) {

                        AsyncImage(
                            model = message.image,
                            contentDescription = "Message image",
                            modifier = Modifier
                                .width(220.dp)
                                .height(220.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    if (!message.text.isNullOrBlank()) {

                        if (!message.image.isNullOrBlank()) {
                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                        }

                        Text(
                            text = message.text
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

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = {
                    showMenu = false
                }
            ) {
                if (canEdit) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {
                            showMenu = false
                            onEdit()
                        }
                    )
                }

                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        showMenu = false
                        onDelete()
                    }
                )
            }
        }
    }
}

private fun uriToMultipart(
    context: Context,
    uri: Uri
): MultipartBody.Part? {

    val contentResolver = context.contentResolver

    val mimeType =
        contentResolver.getType(uri) ?: "image/jpeg"

    val inputStream =
        contentResolver.openInputStream(uri) ?: return null

    val bytes = inputStream.use {
        it.readBytes()
    }

    val requestBody = bytes.toRequestBody(
        mimeType.toMediaTypeOrNull()
    )

    return MultipartBody.Part.createFormData(
        name = "image",
        filename = "image_${System.currentTimeMillis()}",
        body = requestBody
    )
}
