package com.example.yedimap.ui.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yedimap.data.room.entity.ScheduleCell

private val HomePurple = Color(0xFF614184)
private val BgPurple = Color(0xFFE9E2F2)

@Composable
fun ScheduleScreen(
    studentNo: String,
    onBackClick: () -> Unit = {}
) {
    val vm: ScheduleViewModel = viewModel()
    val cells by vm.cells.collectAsState()

    LaunchedEffect(studentNo) {
        vm.load(studentNo)
    }

    // günler
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri") // UI tasarımın bu tarz
    // 09-18 aralığı: 2 saatlik + son 1 saat
    val timeRows = listOf(
        "9AM-10AM" to (9 to 10),
        "10AM-11AM" to (10 to 11),
        "11AM-12AM" to (11 to 12),
        "12AM-1PM" to (12 to 13),
        "1PM-2PM" to (13 to 14),
        "2PM-3PM" to (14 to 15),
        "3PM-4PM" to (15 to 16),
        "4PM-5PM" to (16 to 17),
        "5PM-6PM" to (17 to 18)
    )

    // quick lookup map: (dayOfWeek,startHour) -> cell
    val map = remember(cells) {
        cells.associateBy { it.dayOfWeek to it.startHour }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPurple)
            .padding(horizontal = 14.dp)
    ) {
        // Back
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = HomePurple)
            }
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Schedule",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = HomePurple,
            fontSize = 26.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(14.dp))

        // TABLE
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFB9B2C7))
        ) {

            // Header row: TIME + days
            Row(modifier = Modifier.fillMaxWidth()) {
                HeaderCell(text = "TIME", weight = 1.2f, bg = Color(0xFFBDBDBD))
                days.forEach {
                    HeaderCell(text = it, weight = 1f, bg = HomePurple)
                }
            }

            // Body
            timeRows.forEachIndexed { index, (label, range) ->
                val (startH, endH) = range

                // Lunch row (UI sabit) -> örnek: 1PM-3PM satırında lunch göster
                val isLunchRow = (startH == 13)

                Row(modifier = Modifier.fillMaxWidth()) {
                    TimeCell(text = label, weight = 1.2f)

                    if (isLunchRow) {
                        // Lunch tüm günleri kaplasın (Mon-Fri)
                        Box(
                            modifier = Modifier
                                .weight(5f)
                                .height(64.dp)
                                .border(1.dp, Color(0xFFB9B2C7))
                                .background(Color(0xFF7BC96F)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Lunch",
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        }
                    } else {
                        // Normal 5 day cells
                        for (dayIndex in 1..5) {
                            val cell = map[dayIndex to startH]
                            BodyCell(
                                weight = 1f,
                                cell = cell,
                                height = 64.dp,
                                shaded = (index % 2 == 1)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.HeaderCell(text: String, weight: Float, bg: Color) {
    Box(
        modifier = Modifier
            .weight(weight)
            .height(44.dp)
            .border(1.dp, Color(0xFF3A2E5E))
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun RowScope.TimeCell(text: String, weight: Float) {
    Box(
        modifier = Modifier
            .weight(weight)
            .height(64.dp)
            .border(1.dp, Color(0xFFB9B2C7))
            .background(Color(0xFFEFEAF7)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontSize = 10.sp, color = Color.DarkGray)
    }
}

@Composable
private fun RowScope.BodyCell(
    weight: Float,
    cell: ScheduleCell?,
    height: Dp,
    shaded: Boolean
) {
    val bg = if (cell == null) {
        if (shaded) Color(0xFFEDE7F6) else Color(0xFFF6F2FB)
    } else {
        Color(0xFFE2D8F3)
    }

    Box(
        modifier = Modifier
            .weight(weight)
            .height(height)
            .border(1.dp, Color(0xFFB9B2C7))
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        if (cell != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(cell.courseCode, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                Spacer(Modifier.height(2.dp))
                Text(cell.location, fontSize = 10.sp, color = Color.Black)
            }
        }
    }
}
