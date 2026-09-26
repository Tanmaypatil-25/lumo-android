package com.tanmay.lumo.data.model

data class SidebarUsersResponse(
    val success: Boolean,
    val users: List<User>,
    val unseenMessages: Map<String, Int>
)