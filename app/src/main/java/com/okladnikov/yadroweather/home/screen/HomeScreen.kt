package com.okladnikov.yadroweather.home.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.okladnikov.yadroweather.home.HomeViewModel
import com.okladnikov.yadroweather.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val screenState = viewModel.screenStateFlow.collectAsState().value

    if (screenState.isError) {
        AlertDialog(
            onDismissRequest = { viewModel.processIntent(HomeScreenIntent.CloseError) },
            confirmButton = {
                Button(
                    onClick = { viewModel.processIntent(HomeScreenIntent.CloseError) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text("Close")
                }
            },
            title = { Text(text = "Error") },
            text = { Text(text = screenState.message) },
        )
    }

    Scaffold { paddingValues ->
        PullToRefreshBox(
            modifier = Modifier.padding(paddingValues),
            isRefreshing = screenState.isLoading,
            onRefresh = { viewModel.processIntent(HomeScreenIntent.Refresh) }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                stickyHeader {
                    CurrentWeatherInfo(
                        locationInfo = screenState.locationInfo,
                        temperature = screenState.temperature
                    )
                }

                screenState.weeklyForecast.forEach {
                    item {
                        DayItem(
                            dayName = it.name,
                            temperatureMax = it.temperatureMax
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentWeatherInfo(
    locationInfo: String,
    temperature: String,
) {
    Column(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = locationInfo,
            modifier = Modifier
                .padding(5.dp),
            color = Purple40,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = temperature,
            modifier = Modifier
                .padding(5.dp),
            color = Purple40,
            textAlign = TextAlign.Center,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DayItem(
    dayName: String,
    temperatureMax: String
) {
    ElevatedCard(
        modifier = Modifier.padding(5.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = dayName,
                modifier = Modifier.padding(5.dp)
                    .fillMaxWidth(.4f),
                color = Purple40,
                textAlign = TextAlign.Start,
                fontSize = 25.sp,
            )
            Text(
                text = temperatureMax,
                modifier = Modifier.padding(5.dp)
                    .fillMaxWidth(.3f),
                color = Purple40,
                textAlign = TextAlign.Start,
                fontSize = 25.sp,
            )
        }
    }
}
