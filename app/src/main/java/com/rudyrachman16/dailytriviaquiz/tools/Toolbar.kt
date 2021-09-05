package com.rudyrachman16.dailytriviaquiz.ui

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.rudyrachman16.dailytriviaquiz.ui.theme.Purple500

@Composable
fun Toolbar(title: String) {
    TopAppBar(
        title = { Text(title) },
        backgroundColor = Purple500,
        elevation = 12.dp
    )
}