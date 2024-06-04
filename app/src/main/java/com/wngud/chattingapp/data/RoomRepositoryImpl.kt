package com.wngud.chattingapp.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.wngud.chattingapp.Resource
import com.wngud.chattingapp.domain.RoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
): RoomRepository {

    override suspend fun createRoom(name: String) {
        val room = Room(name = name)
        fireStore.collection("rooms").add(room)
    }

    override suspend fun getRooms(): Flow<Resource<List<Room>>> {
        return flow {
            emit(Resource.Loading(true))
            runCatching {
                val querySnapshot = fireStore.collection("rooms").get().await()
                querySnapshot.documents.map { document ->
                    document.toObject(Room::class.java)!!.copy(id = document.id)
                }
            }.onSuccess {
                Log.i("room", "성공")
                emit(Resource.Success(it))
                emit(Resource.Loading(false))
            }.onFailure {
                Log.i("room", "실패 ${it.message}")
                emit(Resource.Error("Error: ${it.message}"))
            }
        }
    }
}