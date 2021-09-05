package com.rudyrachman16.dailytriviaquiz.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rudyrachman16.dailytriviaquiz.R
import com.rudyrachman16.dailytriviaquiz.ui.quiz.other.MenuActivity
import com.rudyrachman16.dailytriviaquiz.ui.theme.DailyTriviaQuizTheme
import kotlinx.coroutines.delay

@ExperimentalFoundationApi
@ExperimentalAnimationApi
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DailyTriviaQuizTheme {
                SplashScreen {
                    startActivity(Intent(this, MenuActivity::class.java))
                    finish()
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun SplashScreen(goToMenu: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = "Background of Logo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(500.dp)
            )
        }
        LaunchedEffect(key1 = true) {
            delay(1500)
            goToMenu()
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun DefaultSplashPreview() {
    DailyTriviaQuizTheme {
        SplashScreen {}
    }
}