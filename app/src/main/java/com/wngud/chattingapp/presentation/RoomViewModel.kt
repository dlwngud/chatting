package com.wngud.chattingapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wngud.chattingapp.Resource
import com.wngud.chattingapp.data.Room
import com.wngud.chattingapp.domain.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoomState(
    val loading: Boolean = false,
    val rooms: List<Room> = emptyList()
)

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _roomsState = MutableStateFlow(RoomState())
    val roomsState = _roomsState.asStateFlow()

    init {
        loadRooms()
    }

    fun createRoom(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.createRoom(name)
        }
    }

    fun loadRooms() {
        viewModelScope.launch {
            roomRepository.getRooms().collectLatest { result ->
                when(result) {
                    is Resource.Error -> {
                        _roomsState.update { it.copy(loading = false) }
                    }

                    is Resource.Loading -> {
                        _roomsState.update { it.copy(loading = result.isLoading) }
                    }

                    is Resource.Success -> {
                        result.data?.let { rooms ->
                            _roomsState.update { it.copy(rooms = rooms) }
                        }
                    }
                }
            }
        }
    }
}