package com.example.mathgamecompose

import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamePage(navController: NavController, category: String) {
    val systemUiController = rememberSystemUiController()
    val greenColor = colorResource(id = R.color.green)
    systemUiController.setStatusBarColor(color = greenColor)

    val myContext = LocalContext.current

    val life = remember { mutableStateOf(3) }
    val score = remember { mutableStateOf(0) }
    val remainingTimeText = remember { mutableStateOf("30") }
    val myQuestion = remember { mutableStateOf("") }
    val myAnswer = remember { mutableStateOf("") }
    val isOkEnabled = remember { mutableStateOf(true) }
    val isNextEnabled = remember { mutableStateOf(false) }
    val correctAnswer = remember { mutableStateOf(0) }
    val totalTimeInMillis = remember { mutableStateOf(30000L) }
    
    val timer = remember {
        mutableStateOf(
            object : CountDownTimer(totalTimeInMillis.value, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    remainingTimeText.value =
                        String.format(Locale.getDefault(), "%02d", millisUntilFinished / 1000)
                }

                override fun onFinish() {
                    cancel()
                    myQuestion.value = "Sorry, Time is up!"
                    life.value -= 1
                    isOkEnabled.value = false
                    isNextEnabled.value = true
                }

            }.start()
        )
    }

    LaunchedEffect(key1 = category, block = {
        val (question, answer) = generateQuestion(category)
        myQuestion.value = question
        correctAnswer.value = answer
    })

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "back")
                    }
                },
                title = {
                    Text(
                        text = when (category) {
                            "add" -> "Addition"
                            "sub" -> "Subtraction"
                            "multi" -> "Multiplication"
                            else -> "Division"
                        },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = greenColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
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
                .background(backgroundGradient),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(label = "Life", value = life.value.toString(), color = Color.Red)
                StatItem(label = "Score", value = score.value.toString(), color = colorResource(id = R.color.blue))
                StatItem(label = "Time", value = remainingTimeText.value, color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .size(320.dp, 120.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.6f))
                    .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = myQuestion.value,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colorResource(id = R.color.blue),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            TextFieldForAnswer(text = myAnswer)

            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ButtonOkNext(
                    buttonText = "OK",
                    myOnClick = {
                        if (myAnswer.value.isEmpty()) {
                            Toast.makeText(myContext, "Enter an answer!", Toast.LENGTH_SHORT).show()
                        } else {
                            timer.value.cancel()
                            isOkEnabled.value = false
                            isNextEnabled.value = true
                            if (myAnswer.value.toInt() == correctAnswer.value) {
                                score.value += 10
                                myQuestion.value = "Correct!"
                            } else {
                                life.value -= 1
                                myQuestion.value = "Wrong! Ans: ${correctAnswer.value}"
                            }
                        }
                    },
                    isEnabled = isOkEnabled.value
                )

                ButtonOkNext(
                    buttonText = "NEXT",
                    myOnClick = {
                        if (life.value <= 0) {
                            navController.navigate("ResultPage/${score.value}") {
                                popUpTo("HomePage") { inclusive = false }
                            }
                        } else {
                            val (question, answer) = generateQuestion(category)
                            myQuestion.value = question
                            correctAnswer.value = answer
                            myAnswer.value = ""
                            isOkEnabled.value = true
                            isNextEnabled.value = false
                            timer.value.cancel()
                            timer.value.start()
                        }
                    },
                    isEnabled = isNextEnabled.value
                )
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
