package com.mina.weatherapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.lightColorScheme

private val Primary = Color(0xFF2BADEE)
private val BackgroundLight = Color(0xFFF6F7F8)
private val CardLight = Color.White

//@Preview(showBackground = true, widthDp = 411, heightDp = 891)
//@Composable
//fun WeatherForecastScreenPreview() {
//    MaterialTheme(colorScheme = lightColorScheme()) {
//        WeatherForecastScreen()
//    }
//}

private data class InfoCardUi(
    val title: String,
    val value: String,
    val icon: ImageVector
)

private data class HourlyUi(
    val time: String,
    val temp: String,
    val icon: ImageVector,
    val highlighted: Boolean = false
)

private data class DailyUi(
    val day: String,
    val max: String,
    val min: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherForecastScreen(modifier: Modifier = Modifier) {
    val bg = BackgroundLight
    val cardBg = CardLight

    val hourly = remember {
        listOf(
            HourlyUi("12 PM", "26°", Icons.Filled.Home, highlighted = true),
            HourlyUi("1 PM", "27°", Icons.Filled.Home),
            HourlyUi("2 PM", "28°", Icons.Filled.Home),
            HourlyUi("3 PM", "27°", Icons.Filled.Home),
            HourlyUi("4 PM", "25°", Icons.Filled.Home)
        )
    }

    val daily = remember {
        listOf(
            DailyUi("Monday", "26°", "18°", Icons.Filled.Home),
            DailyUi("Tuesday", "28°", "19°", Icons.Filled.Home),
            DailyUi("Wednesday", "25°", "17°", Icons.Filled.Home),
            DailyUi("Thursday", "21°", "15°", Icons.Filled.Home),
            DailyUi("Friday", "23°", "16°", Icons.Filled.Home)
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = bg,
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = bg.copy(alpha = 0.92f)
                ),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Primary.copy(alpha = 0.14f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = null,
                                tint = Primary
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = "Weather Forecast",
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color(0xFF0F172A)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Filled.Search, contentDescription = "Search") }
                    IconButton(onClick = {}) { Icon(Icons.Filled.Refresh, contentDescription = "Refresh") }
                    IconButton(onClick = {}) { Icon(Icons.Filled.LocationOn, contentDescription = "My location", tint = Primary) }
                }
            )
            HorizontalDivider(color = Primary.copy(alpha = 0.10f))
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                HeroSection(
                    temperature = "26°",
                    status = "Clear Sky",
                    location = "London, UK",
                    dateTime = "Monday, 12:00 PM"
                )
            }

            item {
                InfoGrid(
                    items = listOf(
                        InfoCardUi("Humidity", "45%", Icons.Filled.Notifications),
                        InfoCardUi("Wind", "12km/h", Icons.Filled.Refresh),
                        InfoCardUi("Pressure", "1012hPa", Icons.Filled.Settings),
                        InfoCardUi("Clouds", "10%", Icons.Filled.Favorite)
                    ),
                    cardBg = cardBg
                )
            }

            item {
                SectionTitle(title = "Hourly Forecast")
                HourlyRow(items = hourly, cardBg = cardBg)
            }

            item {
                SectionTitle(title = "5-Day Forecast")
                DailyList(items = daily, cardBg = cardBg)
                Spacer(Modifier.height(18.dp))
            }
        }
    }
}

@Composable
private fun HeroSection(
    temperature: String,
    status: String,
    location: String,
    dateTime: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val circleBrush = Brush.linearGradient(
            listOf(Primary.copy(alpha = 0.24f), Primary.copy(alpha = 0.05f))
        )

        Box(
            modifier = Modifier
                .size(192.dp)
                .clip(CircleShape)
                .background(circleBrush),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(64.dp)
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        Text(
            text = temperature,
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Text(
            text = status,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Primary
        )

        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = null,
                tint = Color(0xFF64748B),
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = location,
                fontSize = 14.sp,
                color = Color(0xFF64748B)
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = dateTime.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.6.sp,
            color = Color(0xFF94A3B8)
        )
    }
}

@Composable
private fun InfoGrid(
    items: List<InfoCardUi>,
    cardBg: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        val row1 = items.take(2)
        val row2 = items.drop(2).take(2)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            row1.forEach { item ->
                InfoCard(item, cardBg, Modifier.weight(1f))
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            row2.forEach { item ->
                InfoCard(item, cardBg, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun InfoCard(
    item: InfoCardUi,
    cardBg: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Primary.copy(alpha = 0.06f), RoundedCornerShape(16.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(item.icon, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
            }

            Column {
                Text(
                    text = item.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = item.value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F172A)
    )
}

@Composable
private fun HourlyRow(
    items: List<HourlyUi>,
    cardBg: Color
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
    ) {
        items(items) { it ->
            HourCard(item = it, cardBg = cardBg)
        }
    }
}

@Composable
private fun HourCard(
    item: HourlyUi,
    cardBg: Color
) {
    val container = if (item.highlighted) Primary else cardBg
    val textColor = if (item.highlighted) Color.White else Color(0xFF0F172A)
    val subColor = if (item.highlighted) Color.White.copy(alpha = 0.9f) else Color(0xFF64748B)
    val iconTint = if (item.highlighted) Color.White else Primary

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = container),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.width(96.dp)
    ) {
        Column(
            modifier = Modifier
                .border(1.dp, Primary.copy(alpha = 0.06f), RoundedCornerShape(16.dp))
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = item.time, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = subColor)
            Icon(item.icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(26.dp))
            Text(text = item.temp, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textColor)
        }
    }
}

@Composable
private fun DailyList(
    items: List<DailyUi>,
    cardBg: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.forEach { day ->
            DailyRow(day, cardBg)
        }
    }
}

@Composable
private fun DailyRow(
    item: DailyUi,
    cardBg: Color
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .border(1.dp, Primary.copy(alpha = 0.06f), RoundedCornerShape(16.dp))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.day,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(92.dp),
                color = Color(0xFF0F172A)
            )

            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = Primary
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = item.max, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))
                Text(text = item.min, color = Color(0xFF94A3B8), fontSize = 14.sp)
            }
        }
    }
}