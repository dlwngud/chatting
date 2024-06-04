package com.wngud.chattingapp.domain

import com.wngud.chattingapp.Resource
import com.wngud.chattingapp.data.Room
import kotlinx.coroutines.flow.Flow

interface RoomRepository {

    suspend fun createRoom(name: String)

    suspend fun getRooms(): Flow<Resource<List<Room>>>
}