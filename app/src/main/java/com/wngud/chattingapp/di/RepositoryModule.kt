package com.wngud.chattingapp.di

import com.wngud.chattingapp.data.MessageRepositoryImpl
import com.wngud.chattingapp.data.RoomRepositoryImpl
import com.wngud.chattingapp.data.UserRepositoryImpl
import com.wngud.chattingapp.domain.MessageRepository
import com.wngud.chattingapp.domain.RoomRepository
import com.wngud.chattingapp.domain.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindRoomRepository(roomRepositoryImpl: RoomRepositoryImpl): RoomRepository

    @Binds
    @Singleton
    abstract fun bindMessageRepository(messageRepositoryImpl: MessageRepositoryImpl): MessageRepository
}