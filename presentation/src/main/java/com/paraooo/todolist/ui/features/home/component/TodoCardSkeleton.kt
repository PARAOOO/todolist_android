package com.paraooo.todolist.ui.features.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TodoCardSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            )
            .border(
                width = 1.dp,
                color = Color(0xFFECEEEE),
                shape = RoundedCornerShape(12.dp),
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .padding(start = 19.dp)
                .size(40.dp)
                .background(
                    shape = CircleShape,
                    color = Color(0xFFECEEEE)
                )
        )

        Spacer(modifier = Modifier.width(19.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 18.dp, top = 20.dp, bottom = 18.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(124.dp)
                    .height(12.dp)
                    .background(
                        shape = RoundedCornerShape(100.dp),
                        color = Color(0xFFECEEEE)
                    )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .width(254.dp)
                    .height(8.dp)
                    .background(
                        shape = RoundedCornerShape(100.dp),
                        color = Color(0xFFECEEEE)
                    )
            )

            Spacer(modifier = Modifier.height(4.dp))


            Box(
                modifier = Modifier
                    .width(198.dp)
                    .height(8.dp)
                    .background(
                        shape = RoundedCornerShape(100.dp),
                        color = Color(0xFFECEEEE)
                    )
            )
        }
    }
}