package com.wngud.chattingapp

sealed class Screen(val route: String) {
    object LoginScreen: Screen("loginScreen")
    object SignUpScreen: Screen("signUpScreen")
    object ChatRoomsScreen: Screen("chatRoomsScreen")
    object ChatScreen: Screen("chatScreen")
}