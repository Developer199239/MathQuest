package com.example.mathgamecompose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
//                .background(backgroundGradient),
                .paint(
                    painter = painterResource(id = R.drawable.home_bg),
                    contentScale = ContentScale.FillBounds
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {

            GameCategoryButton(
                text = "Addition ➕",
                onClick = { navController.navigate("GamePage/add") }
            )

            GameCategoryButton(
                text = "Subtraction ➖",
                onClick = { navController.navigate("GamePage/sub") }
            )

            GameCategoryButton(
                text = "Multiplication ✖️",
                onClick = { navController.navigate("GamePage/mutli") }
            )

            GameCategoryButton(
                text = "Division ➗",
                onClick = { navController.navigate("GamePage/div") }
            )
        }
    }
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
        modifier = Modifier.size(280.dp, 90.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
