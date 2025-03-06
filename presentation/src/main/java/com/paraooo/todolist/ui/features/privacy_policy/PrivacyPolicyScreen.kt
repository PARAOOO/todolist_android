package com.paraooo.todolist.ui.features.privacy_policy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.paraooo.todolist.ui.components.TLBoxRounded
import com.paraooo.todolist.ui.components.TLTopbar
import com.paraooo.todolist.ui.components.TLWebView

@Composable
fun PrivacyPolicyScreen(
    navController : NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFF7F7F7)
            )
            .padding(horizontal = 12.dp, vertical = 30.dp)
    ) {

        TLTopbar(
            modifier = Modifier,
            title = "개인정보 처리방침",
            onBackClicked = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(28.dp))

        TLBoxRounded(
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ){
            TLWebView(
//                url = "https://scythe-flight-09e.notion.site/TODO-LIST-1a72d0f6c6aa80b5879cf972607c27e1?pvs=4",
                url = "https://sites.google.com/view/jimmy-todolist-privacy-policy/%ED%99%88"
            )
        }

    }
}