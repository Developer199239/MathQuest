package com.example.mathgamecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mathgamecompose.ui.theme.MathGameComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MathGameComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun MyNavigation(modifier: Modifier = Modifier) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "HomePage",
//            modifier = modifier
        ) {

            composable(
                route = "HomePage"
            ) {
                HomePage(navController = navController)
            }

            composable(
                route = "HistoryPage"
            ) {
                HistoryPage(navController = navController)
            }

            composable(
                route = "GamePage/{category}/{difficulty}",
                arguments = listOf(
                    navArgument("category") { type = NavType.StringType },
                    navArgument("difficulty") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category")
                val difficulty = backStackEntry.arguments?.getString("difficulty")

                if (category != null && difficulty != null) {
                    GamePage(
                        navController = navController,
                        category = category,
                        difficulty = difficulty
                    )
                }
            }

            composable(
                route = "ResultPage/{score}",
                arguments = listOf(
                    navArgument("score") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val userScore = backStackEntry.arguments?.getInt("score")
                userScore?.let { score ->
                    ResultPage(navController = navController, score = score)
                }
            }

        }
    }
}
