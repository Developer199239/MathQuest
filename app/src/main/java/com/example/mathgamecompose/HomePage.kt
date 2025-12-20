package com.example.mathgamecompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {
    val systemUiController = rememberSystemUiController()
    val greenColor = colorResource(id = R.color.green)
    systemUiController.setStatusBarColor(color = greenColor)

    var selectedDifficulty by remember { mutableStateOf("Easy") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Math Game",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = greenColor
                )
            )
        }
    ) { paddingValues ->
        // Modern background with a soft gradient
        val backgroundGradient = Brush.verticalGradient(
            colors = listOf(
                greenColor.copy(alpha = 0.1f),
                colorResource(id = R.color.ice_blue).copy(alpha = 0.2f),
                Color.White
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundGradient)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Difficulty:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
            ) {
                DifficultyChip("Easy", selectedDifficulty == "Easy") { selectedDifficulty = "Easy" }
                DifficultyChip("Medium", selectedDifficulty == "Medium") { selectedDifficulty = "Medium" }
                DifficultyChip("Hard", selectedDifficulty == "Hard") { selectedDifficulty = "Hard" }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                GameCategoryButton(
                    text = "Addition ➕",
                    onClick = { navController.navigate("GamePage/add/$selectedDifficulty") }
                )

                GameCategoryButton(
                    text = "Subtraction ➖",
                    onClick = { navController.navigate("GamePage/sub/$selectedDifficulty") }
                )

                GameCategoryButton(
                    text = "Multiplication ✖️",
                    onClick = { navController.navigate("GamePage/multi/$selectedDifficulty") }
                )

                GameCategoryButton(
                    text = "Division ➗",
                    onClick = { navController.navigate("GamePage/div/$selectedDifficulty") }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifficultyChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(text = text, fontSize = 16.sp) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = colorResource(id = R.color.green),
            selectedLabelColor = Color.White,
            containerColor = Color.White,
            labelColor = colorResource(id = R.color.green)
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = colorResource(id = R.color.green),
            selectedBorderColor = Color.Transparent,
            borderWidth = 1.dp,
            selectedBorderWidth = 0.dp,
            enabled = true,
            selected = isSelected
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun GameCategoryButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.green)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 2.dp
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.size(280.dp, 80.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
