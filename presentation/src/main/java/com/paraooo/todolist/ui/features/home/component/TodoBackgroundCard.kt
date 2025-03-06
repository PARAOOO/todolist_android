package com.paraooo.todolist.ui.features.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.util.circleClickable

@Composable
fun TodoBackgroundCard(
    modifier: Modifier = Modifier,
    onEditClicked : () -> Unit,
    onDeleteClicked : () -> Unit
){

    Row(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Color(0xFFFF6E6E),
                )
                .fillMaxSize()
                .weight(1F)
                .background(
                    color = Color(0xFF529DFF),
                    shape = RoundedCornerShape(
                        topEnd = 12.dp, // 오른쪽 위 모서리
                        bottomEnd = 12.dp // 오른쪽 아래 모서리
                    )
                ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .circleClickable(26.dp) {
                        onEditClicked()
                    }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1F)
                .background(
                    color = Color(0xFFFF6E6E),
                    shape = RoundedCornerShape(
                        topEnd = 12.dp, // 오른쪽 위 모서리
                        bottomEnd = 12.dp // 오른쪽 아래 모서리
                    )
                ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .circleClickable(26.dp) {
                        onDeleteClicked()
                    }
            )
        }
    }

}

@Preview
@Composable
fun PreviewTodoBackgroundCard() {
    TodoBackgroundCard(
        Modifier,{},{}
    )
}