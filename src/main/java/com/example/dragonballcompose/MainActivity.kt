package com.example.dragonballcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.dragonballcompose.ui.navigation.NavGraph
import com.example.dragonballcompose.ui.theme.DragonBallTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            DragonBallTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
