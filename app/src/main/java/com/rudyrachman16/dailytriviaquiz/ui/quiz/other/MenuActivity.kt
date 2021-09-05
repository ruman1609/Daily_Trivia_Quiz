package com.rudyrachman16.dailytriviaquiz.ui.quiz.other

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rudyrachman16.dailytriviaquiz.R
import com.rudyrachman16.dailytriviaquiz.ui.Toolbar
import com.rudyrachman16.dailytriviaquiz.ui.quiz.normal.QuizActivity
import com.rudyrachman16.dailytriviaquiz.ui.theme.DailyTriviaQuizTheme

@ExperimentalFoundationApi
@ExperimentalAnimationApi
class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DailyTriviaQuizTheme {
                val scaffoldState = rememberScaffoldState()
//                val scope = rememberCoroutineScope()
                Scaffold(scaffoldState = scaffoldState, topBar = {
                    Toolbar(title = "Main Menu")
                }) {
                    MainMenu {
//                        scope.launch {
//                            scaffoldState.snackbarHostState.showSnackbar("WAW")
//                        }  // Snackbar
                        startActivity(Intent(this@MenuActivity, QuizActivity::class.java))
                    }
                }
            }
        }
    }
}

@Composable
fun MainMenu(goToQuiz: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(21.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = goToQuiz
        ) {
            Text(stringResource(R.string.gotoquiz))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultMenuPreview() {
    DailyTriviaQuizTheme {
        Scaffold(
            topBar = {
                Toolbar(title = "Main Menu")
            },
        ) {
            MainMenu {

            }
        }
    }
}