package com.wngud.chattingapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wngud.chattingapp.presentation.AuthViewModel
import com.wngud.chattingapp.presentation.ChatRoomListScreen
import com.wngud.chattingapp.presentation.ChatScreen
import com.wngud.chattingapp.ui.theme.ChattingAppTheme
import com.wngud.chattingapp.presentation.LoginScreen
import com.wngud.chattingapp.presentation.MessageViewModel
import com.wngud.chattingapp.presentation.RoomViewModel
import com.wngud.chattingapp.presentation.SignUpScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val authViewModel = hiltViewModel<AuthViewModel>()
            ChattingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(
                        navController = navController,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(navController = navController, startDestination = Screen.SignUpScreen.route) {
        composable(Screen.SignUpScreen.route) { SignUpScreen(navController, authViewModel) }
        composable(Screen.LoginScreen.route) { LoginScreen(navController, authViewModel) }
        composable(Screen.ChatRoomsScreen.route) {
            ChatRoomListScreen(
                navController
            )
        }
        composable(Screen.ChatScreen.route + "/{roomId}") {
            val roomId = it.arguments?.getString("roomId") ?: ""
            ChatScreen(roomId = roomId)
        }
    }
}