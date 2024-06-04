package com.wngud.chattingapp.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wngud.chattingapp.Resource
import com.wngud.chattingapp.domain.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : UserRepository {

    override suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ) {
        auth.createUserWithEmailAndPassword(email, password).await()
        val user = User(firstName, lastName, email)
        saveUserToFireStore(user)
    }

    override suspend fun saveUserToFireStore(user: User) {
        fireStore.collection("users").document(user.email).set(user).await()
    }

    override suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
        Log.i("login", "성공")
    }

    override suspend fun getCurrentUser(): Flow<Resource<User>> {
        return flow {
            emit(Resource.Loading(true))
            runCatching {
                val uid = auth.currentUser?.email
                if (uid != null) {
                    val userDocument = fireStore.collection("users").document(uid).get().await()
                    val user = userDocument.toObject(User::class.java)
                    if (user != null) {
                        Log.i("message", "user: $user")
                        emit(Resource.Success(user))
                    } else {
                        emit(Resource.Error("Error: User data not found"))
                    }
                } else {
                    emit(Resource.Error("Error: User not authenticated"))
                }
            }
        }
    }
}