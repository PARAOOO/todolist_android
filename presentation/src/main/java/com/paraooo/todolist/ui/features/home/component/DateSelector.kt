package com.paraooo.todolist.ui.features.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import java.time.LocalDate
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import com.paraooo.todolist.ui.components.TLBoxRounded
import com.paraooo.domain.util.getDateOfWeekEEE
import com.paraooo.todolist.ui.util.roundedClickable
import kotlinx.coroutines.launch
import java.time.format.TextStyle
import java.util.Locale

fun getDateFromPage(
    page : Int,
) : LocalDate{
    val todayLocalDate = LocalDate.now()
    val pageLocalDate = todayLocalDate.plusDays(
        (page - (Int.MAX_VALUE/2) + 3).toLong()
    )
    return pageLocalDate
}

@Composable
fun DateSelector(
    parentPaddingHorizontal : Dp,
    onDateChange : (date : LocalDate) -> Unit,
    pagerState: PagerState,
    currentPageDebounced : Int,
    setCurrentPageDebounced : (page : Int) -> Unit
) {

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    val horizontalPadding = 10.dp
    val dateSelectorWidthDp = screenWidthDp - (parentPaddingHorizontal * 2) - (horizontalPadding * 2)

    val coroutineScope = rememberCoroutineScope()

    TLBoxRounded(
        modifier = Modifier
            .padding(vertical = 14.dp, horizontal = horizontalPadding),
    ) {

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.isScrollInProgress }
                .collect { isScrolling ->
                    if (!isScrolling) {
                        setCurrentPageDebounced(pagerState.currentPage)
                        onDateChange(getDateFromPage(pagerState.currentPage))
                    }
                }
        }

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 0.dp), // 양쪽 여백
            pageSpacing = 0.dp,
            pageSize = PageSize.Fixed(dateSelectorWidthDp/7),
            modifier = Modifier.fillMaxWidth()
        ) { page: Int ->

            val todayLocalDate = LocalDate.now()
            val pageLocalDate = todayLocalDate.plusDays(
                (page - (Int.MAX_VALUE/2) ).toLong()
            )

            val dayOfWeek = pageLocalDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = dayOfWeek,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = when {
                        todayLocalDate == pageLocalDate -> Color(0xFF39C7A0)
                        dayOfWeek == "Sat" -> Color(0xFF529DFF)
                        dayOfWeek == "Sun" -> Color(0xFFFF6E6E)
                        else -> Color(0xFF7F7F7F)
                    }
                )

                Spacer(modifier = Modifier.height(5.dp))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            shape = CircleShape,
                            color = when {
                                page == currentPageDebounced + 3 -> Color(0x2054C392)
                                else -> Color.White
                            }
                        )
                        .border(
                            width = 1.dp,
                            color = when {
                                page == currentPageDebounced + 3 -> Color(0xFF54C392)
                                else -> Color(0xFFF2F2F2)
                            },
                            shape = CircleShape
                        ).roundedClickable(100.dp) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page - 3)
                            }
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "${pageLocalDate.dayOfMonth}",
                        modifier = Modifier,
                        fontFamily = PretendardFontFamily,
                        fontWeight = when {
                            page == currentPageDebounced+3 -> FontWeight.SemiBold
                            else -> FontWeight.Normal
                        },
                        fontSize = 14.sp,
                        color = when {
                            page == currentPageDebounced+3-> Color(0xFF54C392)
                            else -> Color(0xFF777777)
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewDateSelector() {
    DateSelector(
        parentPaddingHorizontal = 0.dp,
        onDateChange = {},
        rememberPagerState(pageCount = {0}),
        currentPageDebounced = 0,
        setCurrentPageDebounced = {}
    )
}