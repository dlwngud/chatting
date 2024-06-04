package com.wngud.chattingapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wngud.chattingapp.Resource
import com.wngud.chattingapp.data.Message
import com.wngud.chattingapp.data.User
import com.wngud.chattingapp.domain.MessageRepository
import com.wngud.chattingapp.domain.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MessagesState(
    val loading: Boolean = false,
    val messages: List<Message> = emptyList(),
    val roomId: String? = null,
    val currentUser: User? = null
)

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _messages = MutableStateFlow(MessagesState())
    val messages = _messages.asStateFlow()

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            userRepository.getCurrentUser().collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _messages.update { it.copy(loading = false) }
                    }

                    is Resource.Loading -> {
                        _messages.update { it.copy(loading = result.isLoading) }
                    }

                    is Resource.Success -> {
                        result.data?.let { user ->
                            _messages.update { it.copy(currentUser = user) }
                        }
                    }
                }
            }
        }
    }

    fun loadMessages() {
        viewModelScope.launch {
            if (_messages.value.roomId != null) {
                messageRepository.getChatMessages(_messages.value.roomId!!)
                    .collectLatest { messageList ->
                        _messages.update {
                            it.copy(messages = messageList)
                        }
                    }
            }
        }
    }

    fun sendMessage(text: String) {
        if (_messages.value.currentUser != null) {
            val message = Message(
                senderFirstName = _messages.value.currentUser!!.firstName,
                senderId = _messages.value.currentUser!!.email,
                text = text
            )
            viewModelScope.launch {
                messageRepository.sendMessage(_messages.value.roomId!!, message)
            }
        }
    }

    fun setRoomId(roomId: String) {
        _messages.update { it.copy(roomId = roomId) }
        loadMessages()
    }
}