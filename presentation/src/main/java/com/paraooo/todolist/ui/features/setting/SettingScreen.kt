package com.paraooo.todolist.ui.features.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.components.TLBoxRounded
import com.paraooo.todolist.ui.components.TLTopbar
import com.paraooo.todolist.ui.navigation.Destinations
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.todolist.ui.util.circleClickable
import com.paraooo.todolist.ui.util.roundedClickable
import com.paraooo.todolist.BuildConfig

@Composable
fun SettingScreen(
    navController : NavController
) {

    fun String.parseVersionName(): String {
        return this.replace(".", ". ")
    }

    val versionName = BuildConfig.VERSION_NAME

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFF7F7F7)
            )
            .padding(horizontal = 12.dp, vertical = 30.dp)
    ) {
        TLTopbar(
            title = "설정",
            onBackClicked = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(28.dp))

        TLBoxRounded (
            modifier = Modifier.fillMaxWidth().padding(18.dp)
        ){
            Column {
                Text(
                    text = "앱 버전",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color(0xFF7F7F7F),
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "v ${versionName.parseVersionName()}",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color(0xFF545454),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = CircleShape)
                        .border(1.dp, Color(0xFFECEEEE), CircleShape)
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "개인정보 처리방침",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color(0xFF7F7F7F),
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = CircleShape)
                        .border(1.dp, Color(0xFFECEEEE), CircleShape)
                        .roundedClickable(cornerRadius = 100.dp){
                            navController.navigate(Destinations.PrivacyPolicy.route)
                        }.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "개인정보 처리방침",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color(0xFF545454),
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_back_thin),
                        contentDescription = "right arrow",
                        modifier = Modifier
                            .height(10.dp)
                            .padding(end = 2.dp)
                    )

                }


            }
        }
    }
}

@Preview
@Composable
fun PreviewSettingScreen() {
    SettingScreen(
        rememberNavController()
    )
}