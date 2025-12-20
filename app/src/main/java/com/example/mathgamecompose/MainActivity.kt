package com.example.mathgamecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
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
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    MyNavigation()
                }
            }
        }
    }

    @Composable
    fun MyNavigation() {

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "HomePage"
        ) {

            composable(
                route = "HomePage"
            ) {
                HomePage(navController = navController)
            }

            composable(
                route = "GamePage/{category}",
                arguments = listOf(
                    navArgument("category"){type = NavType.StringType}
                )
            ){
                val selectedCategory = it.arguments?.getString("category")

                selectedCategory?.let { category ->
                    GamePage(navController = navController, category = category)
                }
            }

            composable(
                route = "ResultPage/{score}",
                arguments = listOf(
                    navArgument("score"){type = NavType.IntType}
                )
            ){
                val userScore = it.arguments?.getInt("score")
                userScore?.let { score ->
                    ResultPage(navController = navController, score = score)
                }
            }

        }
    }
}
