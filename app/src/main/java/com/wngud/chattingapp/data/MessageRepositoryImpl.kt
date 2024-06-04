package com.wngud.chattingapp.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.wngud.chattingapp.domain.MessageRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : MessageRepository {

    override suspend fun sendMessage(roomId: String, message: Message) {
        Log.i("message", "성공")
        fireStore.collection("rooms").document(roomId)
            .collection("messages").add(message).await()
    }

    override fun getChatMessages(roomId: String): Flow<List<Message>> = callbackFlow {
        val subscription = fireStore.collection("rooms").document(roomId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { querySnapshot, _ ->
                querySnapshot?.let {
                    trySend(it.documents.map { doc ->
                        doc.toObject(Message::class.java)!!.copy()
                    }).isSuccess
                }
            }

        awaitClose { subscription.remove() }
    }
}