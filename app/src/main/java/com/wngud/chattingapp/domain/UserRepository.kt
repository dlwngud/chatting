package com.wngud.chattingapp.domain

import com.wngud.chattingapp.Resource
import com.wngud.chattingapp.data.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    )

    suspend fun saveUserToFireStore(user: User)

    suspend fun login(email: String, password: String)

    suspend fun getCurrentUser(): Flow<Resource<User>>
}