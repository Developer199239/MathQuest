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
import androidx.compose.ui.text.style.TextAlign
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
        // Modern Dark Background with Gradient
        val backgroundGradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1A262D),
                Color(0xFF263238)
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
            // Max Scores Card with dark theme
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🏆", fontSize = 28.sp)
                        Spacer(modifier = Modifier.size(12.dp))
                        Text(
                            text = "Max Scores",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    MaxScoreRow("Easy", easyMax, Color(0xFF4CAF50))
                    MaxScoreRow("Medium", mediumMax, Color(0xFFFF9800))
                    MaxScoreRow("Hard", hardMax, Color(0xFFF44336))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Recent Games",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.Start).padding(start = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // History List with Modern Gradient Overlay
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.withAlpha(0.1f),
                                Color.White.withAlpha(0.05f)
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.Top
            ) {
                items(historyList) { record ->
                    HistoryItem(record)
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = Color.White.copy(alpha = 0.1f)
                    )
                }
            }
        }
    }
}

// Extension to mimic withAlpha if not available directly on Color
fun Color.withAlpha(alpha: Float): Color = this.copy(alpha = alpha)

@Composable
fun MaxScoreRow(difficulty: String, score: Int, indicatorColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$difficulty:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.7f)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = score.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.size(12.dp))
            Box(modifier = Modifier.size(12.dp).background(indicatorColor, RoundedCornerShape(6.dp)))
        }
    }
}

@Composable
fun HistoryItem(record: GameRecord) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = record.date,
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.4f),
                fontWeight = FontWeight.Normal
            )
        }
        
        Text(
            text = "Score: ${record.score}",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        
        Text(
            text = record.difficulty,
            fontSize = 16.sp,
            color = when(record.difficulty) {
                "Easy" -> Color(0xFF4CAF50)
                "Medium" -> Color(0xFFFF9800)
                "Hard" -> Color(0xFFF44336)
                else -> Color.White
            },
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.7f),
            textAlign = TextAlign.End
        )
    }
}

data class GameRecord(val date: String, val score: Int, val difficulty: String)
