package com.rudyrachman16.dailytriviaquiz.ui.quiz.normal

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rudyrachman16.back_end.data.Status
import com.rudyrachman16.back_end.domain.model.Quiz
import com.rudyrachman16.back_end.utils.Constant
import com.rudyrachman16.back_end.utils.Dummy
import com.rudyrachman16.back_end.utils.SaveAnswer
import com.rudyrachman16.dailytriviaquiz.R
import com.rudyrachman16.dailytriviaquiz.ViewModelFactory
import com.rudyrachman16.dailytriviaquiz.tools.ErrorMessage
import com.rudyrachman16.dailytriviaquiz.tools.Loading
import com.rudyrachman16.dailytriviaquiz.ui.Toolbar
import com.rudyrachman16.dailytriviaquiz.ui.quiz.other.ListQuizActivity
import com.rudyrachman16.dailytriviaquiz.ui.quiz.other.ResultActivity
import com.rudyrachman16.dailytriviaquiz.ui.theme.DailyTriviaQuizTheme
import com.rudyrachman16.dailytriviaquiz.ui.theme.Purple700
import java.util.*

@ExperimentalAnimationApi
@ExperimentalFoundationApi
class QuizActivity : ComponentActivity() {
    companion object {
        @JvmStatic
        private var number = 0

        const val NUMBER = "com.rudyrachman16.dailytriviaquiz.ui.quiz.normal.ui.NUMBER"
        const val TITLE = "Normal Quiz"
    }

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
        number = intent.getIntExtra(NUMBER, number)
        viewModel.getQuiz(Constant.AMOUNT).observe(this) { status ->
            setContent {
                DailyTriviaQuizTheme {
                    val dialogComplete = remember { mutableStateOf(false) }
                    if (status != null) {
                        when (status) {
                            is Status.Success -> {
                                val results = status.data!!.results
                                QuizBody(
                                    answers, results[number], number, results.size - 1,
                                    {
                                        // Go to List of Quiz
                                        startActivity(
                                            Intent(this, ListQuizActivity::class.java).apply {
                                                putExtra(NUMBER, number)
                                            })
                                        finish()
                                    },
                                    {
                                        // SAVE
                                        save.putAnswers(Gson().toJson(it))
                                    },
                                    {
                                        // PREVIOUS
                                        nextPrevQuiz().apply { number-- }
                                    }, {
                                        // NEXT
                                        nextPrevQuiz().apply { number++ }
                                    }) {
                                    // COMPLETE
                                    dialogComplete.value = true
                                }
                            }
                            is Status.Loading -> Loading()
                            is Status.Error -> ErrorMessage(status.error!!)
                        }
                    }
                    if (dialogComplete.value) Confirmation(dialogComplete) {
                        startActivity(Intent(this, ResultActivity::class.java))
                        number = 0
                        finish()
                    }
                }
            }
        }
    }

    private fun nextPrevQuiz() {
        startActivity(Intent(this, QuizActivity::class.java))
        finish()
    }
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun QuizBody(
    answers: MutableList<String>, quiz: Quiz, number: Int, limit: Int,
    listGo: (Int) -> Unit, save: (MutableList<String>) -> Unit,
    previous: () -> Unit, next: () -> Unit, complete: () -> Unit
) {
    Scaffold(topBar = { Toolbar(QuizActivity.TITLE) }, bottomBar = {
        NavigateQuiz(
            number = number, limit = limit, previous = { previous() }, { next() }) {
            complete()
        }
    }, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .padding(21.dp)
        ) {
            Question(number + 1, quiz.question, quiz.difficulty ?: "", listGo)
            Options(quiz.answers, quiz.type, answers[number]) {
                answers[number] = it
                save(answers)
            }
        }
    }
}

@Composable
fun Question(number: Int, question: String, difficulty: String, listGo: (Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Number. $number ${if (difficulty != "") "($difficulty)" else ""}",
                fontSize = 16.sp
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_grid),
                contentDescription = "List Qustion",
                tint = Color.Black, modifier = Modifier.clickable { listGo(number - 1) }
            )
        }
        Text(
            text = question,
            fontSize = 24.sp,
            modifier = Modifier.padding(0.dp, 10.dp, 10.dp, 10.dp),
        )
    }
}

@ExperimentalFoundationApi
@Composable
fun Options(answers: List<String>, type: String, choice: String, answerClick: (String) -> Unit) {
    val choiceState = remember { mutableStateOf(choice) }
    Text(
        text = "${
            type.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
        } Choice",
        fontSize = 16.sp,
        color = Color.DarkGray,
        modifier = Modifier
            .offset(x = 16.dp, y = 10.dp)
            .background(Color.White)
            .zIndex(1f)
            .padding(8.dp, 0.dp)
    )
    if (type == "multiple") {
        LazyColumn(
            modifier = Modifier
                .border(width = 5.dp, color = Purple700)
                .fillMaxWidth(),
            contentPadding = PaddingValues(15.dp),
        ) {
            itemsIndexed(items = answers) { index, answer ->
                OptionMultiple(answer, index, choiceState, answerClick)
            }
        }
    } else {
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            modifier = Modifier
                .border(width = 5.dp, color = Purple700)
                .fillMaxWidth(),
            contentPadding = PaddingValues(15.dp)
        ) {
            items(items = answers) {
                OptionBoolean(it.toBoolean(), choiceState, answerClick)
            }
        }
    }
}

@Composable
fun OptionMultiple(
    answer: String,
    index: Int,
    choice: MutableState<String>,
    answerClick: (String) -> Unit
) {
    val backgroundColor by animateColorAsState(
        if (choice.value == answer) Color(35, 57, 93) else Color.White
    )
    val color by animateColorAsState(
        if (choice.value == answer) Color.White else Color.Black
    )
    Card(
        shape = RoundedCornerShape(5.dp), modifier = Modifier.padding(15.dp),
        backgroundColor = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    choice.value = answer
                    answerClick(answer)
                }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = RoundedCornerShape(5.dp, 0.dp, 0.dp, 5.dp),
                backgroundColor = Purple700,
                modifier = Modifier
                    .fillMaxWidth(.1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = when (index) {
                        0 -> "A"
                        1 -> "B"
                        2 -> "C"
                        3 -> "D"
                        else -> ""
                    },
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.padding(5.dp),
                    textAlign = TextAlign.Center,
                )
            }
            Text(
                text = answer,
                fontSize = 20.sp,
                modifier = Modifier.padding(5.dp, 5.dp),
                color = color
            )
        }
    }
}

@Composable
fun OptionBoolean(answer: Boolean, choice: MutableState<String>, answerClick: (String) -> Unit) {
    val backgroundColor by animateColorAsState(
        if (choice.value == answer.toString()) Color.Yellow
        else Purple700
    )
    val color by animateColorAsState(
        if (choice.value == answer.toString()) Color.Black else Color.White
    )
    Column(
        modifier = Modifier
            .zIndex(1f)
            .fillMaxWidth()
            .padding(0.dp, 8.dp, 0.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(color)
                .padding(2.dp)
                .clickable {
                    choice.value = answer.toString()
                    answerClick(answer.toString())
                }
        ) {
            Image(
                painter =
                if (answer) painterResource(id = R.drawable.ic_check)
                else painterResource(id = R.drawable.ic_cancel),
                contentDescription = stringResource(
                    id = R.string.answer_boolean,
                    answer.toString()
                ),
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    Card(
        shape = RoundedCornerShape(5.dp), modifier = Modifier
            .padding(24.dp)
            .height(65.dp)
            .shadow(5.dp)
            .clickable {
                choice.value = answer.toString()
                answerClick(answer.toString())
            },
        backgroundColor = backgroundColor
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = answer.toString(),
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp),
                color = color
            )
        }
    }
}

@Composable
fun NavigateQuiz(
    number: Int,
    limit: Int,
    previous: () -> Unit,
    next: () -> Unit,
    complete: () -> Unit
) {
    val arrangement = when (number) {
        0 -> Arrangement.End
        else -> Arrangement.SpaceBetween
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 40.dp, 0.dp, 0.dp),
        horizontalArrangement = arrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (number != 0) NavigateButton(
            text = "Previous", Modifier.offset(21.dp, 0.dp)
        ) { previous() }
        if (number != limit) NavigateButton(
            text = "Next", Modifier.offset((-21).dp, 0.dp)
        ) { next() }
        else NavigateButton(
            text = "Complete", Modifier.offset((-21).dp, 0.dp)
        ) { complete() }
    }
}

@Composable
fun NavigateButton(text: String, modifier: Modifier, command: () -> Unit) {
    Card(
        modifier = modifier
            .width(150.dp)
            .padding(0.dp, 20.dp)
            .clickable { command() },
        shape = RoundedCornerShape(5.dp), backgroundColor = Purple700
    ) {
        Text(
            text,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(0.dp, 10.dp),
            fontSize = 20.sp
        )
    }
}

@Composable
fun Confirmation(dialogComplete: MutableState<Boolean>, goToResult: () -> Unit) {
    AlertDialog(
        onDismissRequest = { dialogComplete.value = false },
        title = { Text("Confirmation") },
        text = { Text("Press Confirm for see result") },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                val modifier = Modifier.width(135.dp)
                Button(onClick = {
                    dialogComplete.value = false
                }, modifier = modifier) { Text("Cancel") }
                Button(onClick = {
                    dialogComplete.value = false
                    goToResult()
                }, modifier = modifier) { Text("Confirmation") }
            }
        }
    )
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreviewQuiz() {
    DailyTriviaQuizTheme {
        QuizBody(
            mutableListOf("", "", "", "", "", "", "", "", "", ""),
            Dummy.boolean, 1, 2, {}, {}, {}, {}) {}
    }
}