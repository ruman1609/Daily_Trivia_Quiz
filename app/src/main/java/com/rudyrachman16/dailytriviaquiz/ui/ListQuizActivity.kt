package com.rudyrachman16.dailytriviaquiz.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rudyrachman16.back_end.data.Status
import com.rudyrachman16.back_end.domain.model.Quiz
import com.rudyrachman16.back_end.utils.Constant
import com.rudyrachman16.back_end.utils.Dummy
import com.rudyrachman16.back_end.utils.SaveAnswer
import com.rudyrachman16.dailytriviaquiz.ViewModelFactory
import com.rudyrachman16.dailytriviaquiz.tools.ErrorMessage
import com.rudyrachman16.dailytriviaquiz.tools.Loading
import com.rudyrachman16.dailytriviaquiz.ui.quiz.normal.NormalQuizViewModel
import com.rudyrachman16.dailytriviaquiz.ui.quiz.normal.ui.QuizActivity
import com.rudyrachman16.dailytriviaquiz.ui.quiz.normal.ui.theme.DailyTriviaQuizTheme
import com.rudyrachman16.dailytriviaquiz.ui.quiz.normal.ui.theme.Purple700

@ExperimentalAnimationApi
@ExperimentalFoundationApi
class ListQuizActivity : AppCompatActivity() {
    companion object {
        const val TITLE = "List of Quiz"
    }

    private var number = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: NormalQuizViewModel by viewModels {
            ViewModelFactory.getInstance()
        }
        val save = SaveAnswer(applicationContext)
        val answers =
            if (save.getAnswers() == "") MutableList(Constant.AMOUNT) { "" }
            else Gson().fromJson(
                save.getAnswers(),
                object : TypeToken<MutableList<String>>() {}.type
            )
        number = intent.getIntExtra(QuizActivity.NUMBER, 0)

        viewModel.getQuiz(Constant.AMOUNT).observe(this) { status ->
            setContent {
                DailyTriviaQuizTheme {
                    if (status != null) {
                        when (status) {
                            is Status.Success ->
                                ListQuiz(status.data!!.results, answers, ::navigateToQuiz)
                            is Status.Loading -> Loading()
                            is Status.Error -> ErrorMessage(message = status.error!!)
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        navigateToQuiz(number)
    }

    private fun navigateToQuiz(number: Int) {
        startActivity(Intent(this, QuizActivity::class.java).apply {
            putExtra(QuizActivity.NUMBER, number)
        })
        finish()
    }
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ListQuiz(listQuiz: List<Quiz>, answers: MutableList<String>, click: (Int) -> Unit) {
    Scaffold(topBar = { Toolbar(title = ListQuizActivity.TITLE) }) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(5),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(21.dp)
        ) {
            itemsIndexed(items = listQuiz) { number, quiz ->
                ItemQuiz(quiz, number + 1, answers[number], click)
            }
        }
    }
}

@Composable
fun ItemQuiz(quiz: Quiz, number: Int, answer: String, click: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .padding(0.dp, 5.dp)
            .wrapContentSize()
    ) {
        val color = if (answer == "") Color.Black else Color.White
        val secColor = if (answer == "") Color.DarkGray else Color.LightGray
        val backgroundColor = if (answer == "") Color.White else Color(35, 57, 93)
        Card(
            modifier = Modifier
                .border(1.dp, Purple700)
                .size(60.dp)
                .clickable { click(number - 1) },
            contentColor = backgroundColor
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = number.toString(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = color,
                    modifier = Modifier.wrapContentSize()
                )
                Text(
                    text = quiz.type.lowercase(),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = secColor,
                    modifier = Modifier.wrapContentSize()
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreviewListQuiz() {
    DailyTriviaQuizTheme {
        ListQuiz(
            listOf(
                Dummy.boolean, Dummy.multiply,
                Dummy.boolean, Dummy.multiply,
                Dummy.boolean, Dummy.multiply
            ), MutableList(6) { "" }
        ) {}
    }
}