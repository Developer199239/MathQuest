package com.example.mathgamecompose

import kotlin.random.Random

fun generateQuestion(selectedCategory: String, difficulty: String?): Pair<String, Int> {

    val range = when (difficulty) {
        "Easy" -> 0..20
        "Medium" -> 0..100
        "Hard" -> 0..500
        else -> 0..100
    }

    val multiRange = when (difficulty) {
        "Easy" -> 0..10
        "Medium" -> 0..15
        "Hard" -> 0..30
        else -> 0..15
    }

    var number1 = Random.nextInt(range.first, range.last + 1)
    var number2 = Random.nextInt(range.first, range.last + 1)

    val textQuestion: String
    val correctAnswer: Int

    when (selectedCategory) {
        "add" -> {
            textQuestion = "$number1 + $number2"
            correctAnswer = number1 + number2
        }
        "sub" -> {
            if (number1 >= number2) {
                textQuestion = "$number1 - $number2"
                correctAnswer = number1 - number2
            } else {
                textQuestion = "$number2 - $number1"
                correctAnswer = number2 - number1
            }
        }
        "multi" -> {
            number1 = Random.nextInt(multiRange.first, multiRange.last + 1)
            number2 = Random.nextInt(multiRange.first, multiRange.last + 1)
            textQuestion = "$number1 * $number2"
            correctAnswer = number1 * number2
        }
        else -> { // Division
            // Generate a divisor first
            val divisor = Random.nextInt(multiRange.first.coerceAtLeast(1), multiRange.last + 1)
            // Generate a quotient (correctAnswer)
            val quotient = Random.nextInt(multiRange.first, multiRange.last + 1)
            // Calculate the dividend
            val dividend = divisor * quotient
            
            textQuestion = "$dividend / $divisor"
            correctAnswer = quotient
        }
    }

    return Pair(textQuestion, correctAnswer)
}
