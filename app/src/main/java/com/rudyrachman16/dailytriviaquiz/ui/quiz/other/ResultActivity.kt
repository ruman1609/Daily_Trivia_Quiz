package com.rudyrachman16.dailytriviaquiz.ui.quiz.other

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rudyrachman16.back_end.data.Status
import com.rudyrachman16.back_end.utils.Constant
import com.rudyrachman16.back_end.utils.SaveAnswer
import com.rudyrachman16.dailytriviaquiz.ViewModelFactory
import com.rudyrachman16.dailytriviaquiz.tools.ErrorMessage
import com.rudyrachman16.dailytriviaquiz.tools.Loading
import com.rudyrachman16.dailytriviaquiz.ui.Toolbar
import com.rudyrachman16.dailytriviaquiz.ui.quiz.normal.NormalQuizViewModel
import com.rudyrachman16.dailytriviaquiz.ui.theme.DailyTriviaQuizTheme
import kotlinx.coroutines.delay

@ExperimentalAnimationApi
class ResultActivity : AppCompatActivity() {
    companion object {
        const val TITLE = "Result"
    }

    private val viewModel: NormalQuizViewModel by viewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val save = SaveAnswer(applicationContext)
        val answers =
            if (save.getAnswers() == "") MutableList(Constant.AMOUNT) { "" }
            else Gson().fromJson(
                save.getAnswers(),
                object : TypeToken<MutableList<String>>() {}.type
            )
        viewModel.getQuiz(Constant.AMOUNT).observe(this) { status ->
            setContent {
                DailyTriviaQuizTheme {
                    when (status) {
                        is Status.Success -> ShowResult(
                            answers,
                            status.data!!.results.map { it.correctAnswer }
                        )
                        is Status.Loading -> Loading()
                        is Status.Error -> ErrorMessage(message = status.error!!)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.resetDefault()
    }
}

@ExperimentalAnimationApi
@Composable
fun ShowResult(answers: List<String>, correctAnswers: List<String>) {
    var result = 0
    answers.forEachIndexed { index, answer ->
        if (answer == correctAnswers[index]) result++
    }
    Scaffold(topBar = { Toolbar(ResultActivity.TITLE) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(21.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Your Result:", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(15.dp))
            val transition =
                slideInVertically({ height -> height }) + fadeIn() with
                        slideOutVertically({ height -> -height }) + fadeOut()
            var zeroCorrect by remember { mutableStateOf(0) }
            var zeroValue by remember { mutableStateOf(0) }
            val value = (result.toFloat() / correctAnswers.size.toFloat() * 100).toInt()
            Row {
                AnimatedContent(
                    targetState = zeroCorrect,
                    transitionSpec = { transition }) { valueTarget ->
                    Text(text = "$valueTarget ", fontSize = 20.sp)
                }
                Text(text = "/ ${correctAnswers.size}", fontSize = 20.sp, color = Color.Gray)
            }
            AnimatedContent(
                targetState = zeroValue,
                transitionSpec = { transition }) { valueTarget ->
                Text(
                    text = "$valueTarget%", fontSize = 24.sp
                )
            }
            LaunchedEffect(key1 = zeroCorrect, key2 = zeroValue) {
                if (zeroCorrect < result) {
                    delay(80)
                    zeroCorrect++
                } else {
                    if (zeroValue < value) {
                        delay(10)
                        zeroValue += 5
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreviewResultQuiz() {
    DailyTriviaQuizTheme {
        ShowResult(listOf("a", "b"), listOf("a", "a"))
    }
}