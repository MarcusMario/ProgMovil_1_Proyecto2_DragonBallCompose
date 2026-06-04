package com.example.dragonballcompose.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Search : Screen("search")
    object Detail : Screen("detail/{characterId}") {
        fun createRoute(characterId: Int) = "detail/$characterId"
    }
}
