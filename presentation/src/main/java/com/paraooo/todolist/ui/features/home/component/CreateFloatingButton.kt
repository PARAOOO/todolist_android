package com.paraooo.todolist.ui.features.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.paraooo.todolist.R

@Composable
fun CreateFloatingButton(
    onClick : () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .background(
                color = Color(0xFF54C392),
                shape = CircleShape
            )
            .size(60.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF54C392))
    ) {
        Image(
            modifier = Modifier.size(30.dp),
            painter = painterResource(R.drawable.ic_add),
            contentDescription = null
        )
    }
}