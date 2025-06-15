package com.example.onetouchpromise.util

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.topBarPadding(): Modifier = this.then(Modifier.padding(top = 16.dp))