package com.paraooo.todolist.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.paraooo.todolist.ui.theme.PretendardFontFamily

@Composable
fun TLDialog(
    modifier: Modifier = Modifier,
    showDialog : Boolean,
    onDismiss: () -> Unit,
    content: String,
    bottomComponent : @Composable () -> Unit = {}
) {

    if(showDialog){
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            Box(
                modifier = modifier
            ){
                TLBoxRounded(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp, horizontal = 24.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = content,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = Color.Black,
                            softWrap = true
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        bottomComponent()
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun PreviewTLDialog() {
    TLDialog(
        showDialog = true,
        onDismiss = {},
        content = "확인을 누르면 이전 화면으로 돌아갑니다. \n작성 중인 Todo는 저장되지 않습니다."
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "취소",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color(0xFF545454),
            )

            Spacer(modifier = Modifier.width(40.dp))

            Text(
                text = "확인",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color(0xFF545454),
            )
        }
    }
}