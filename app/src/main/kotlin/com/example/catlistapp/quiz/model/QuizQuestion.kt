package raf.rma.catapult.quiz.model

import com.example.catlistapp.quiz.model.QuestionType

data class QuizQuestion(
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
    val options: List<String>,
    val imageUrl: String? = null,
    val questionType: QuestionType
)
