package com.example.mathgamecompose

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryPage(navController: NavController) {
    val systemUiController = rememberSystemUiController()
    val greenColor = colorResource(id = R.color.green)
    systemUiController.setStatusBarColor(color = greenColor)

    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("MathGamePrefs", Context.MODE_PRIVATE)
    }

    val easyMax = sharedPreferences.getInt("max_Easy", 0)
    val mediumMax = sharedPreferences.getInt("max_Medium", 0)
    val hardMax = sharedPreferences.getInt("max_Hard", 0)

    // Using a simple string format "date|score|difficulty" stored in a Set
    val historySet = sharedPreferences.getStringSet("game_history", emptySet()) ?: emptySet()
    val historyList = historySet.map {
        val parts = it.split("|")
        GameRecord(parts[0], parts[1].toInt(), parts[2])
    }.sortedByDescending { it.date }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "back", tint = Color.White)
                    }
                },
                title = {
                    Text(
                        text = "History",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = greenColor
                )
            )
        }
    ) { paddingValues ->
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Max Scores Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🏆", fontSize = 24.sp)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "Max Scores", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    MaxScoreRow("Easy", easyMax, Color(0xFF4CAF50))
                    MaxScoreRow("Medium", mediumMax, Color(0xFFFF9800))
                    MaxScoreRow("Hard", hardMax, Color(0xFFF44336))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Recent Games",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(historyList) { record ->
                    HistoryItem(record)
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                }
            }
        }
    }
}

@Composable
fun MaxScoreRow(difficulty: String, score: Int, indicatorColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$difficulty:", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = score.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.size(8.dp))
            Box(modifier = Modifier.size(12.dp).background(indicatorColor, RoundedCornerShape(6.dp)))
        }
    }
}

@Composable
fun HistoryItem(record: GameRecord) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = record.date, fontSize = 14.sp, color = Color.Gray)
        Text(text = "Score: ${record.score}", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Text(
            text = record.difficulty,
            fontSize = 14.sp,
            color = when(record.difficulty) {
                "Easy" -> Color(0xFF4CAF50)
                "Medium" -> Color(0xFFFF9800)
                "Hard" -> Color(0xFFF44336)
                else -> Color.Black
            },
            fontWeight = FontWeight.SemiBold
        )
    }
}

data class GameRecord(val date: String, val score: Int, val difficulty: String)
