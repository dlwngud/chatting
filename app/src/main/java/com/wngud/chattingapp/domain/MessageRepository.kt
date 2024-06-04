package com.wngud.chattingapp.domain

import com.wngud.chattingapp.data.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun sendMessage(roomId: String, message: Message)

    fun getChatMessages(roomId: String): Flow<List<Message>>
}