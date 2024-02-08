/*
 * Copyright 2024
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.edwardmuturi.forecast.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edwardmuturi.forecast.R
import com.edwardmuturi.forecast.utils.DateUtils.getNextFiveDays

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ForecastScreen(forecastViewModel: ForecastViewModel = viewModel()) {
    val forecastScreenUiState by forecastViewModel.forecastUiState.collectAsState()
    val locationState by forecastViewModel.currentLocation.collectAsState()
    val context = LocalContext.current
    val pullRefreshState = rememberPullRefreshState(
        refreshing = forecastScreenUiState.isLoading,
        onRefresh = { forecastViewModel.loadForecast(locationState.latitude, locationState.longitude) }
    )

    LaunchedEffect(
        key1 = locationState.latitude,
        block = {
            forecastViewModel.loadForecast(locationState.latitude, locationState.longitude)
        }
    )

    Scaffold(containerColor = BackgroundColor(forecastScreenUiState.currentDayForecastUiState.forecast?.type)) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
                .pullRefresh(pullRefreshState)
        ) {
            if (!forecastScreenUiState.isLoading) {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .testTag("ForecastColumn")
                ) {
                    stickyHeader {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Image(
                                painter = painterResource(id = getBackgroundImage(forecastScreenUiState.currentDayForecastUiState.forecast?.type)),
                                contentDescription = stringResource(R.string.text_weather_type_image),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.TopCenter),
                                contentScale = ContentScale.Crop
                            )
                            Column(Modifier.align(Alignment.Center)) {
                                Text(
                                    text = forecastScreenUiState.currentDayForecastUiState.forecast?.currentTemp.toString()
                                        .plus(context.getString(R.string.degree_symbol)),
                                    fontSize = 52.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 50.dp)
                                )

                                Text(
                                    text = forecastScreenUiState.currentDayForecastUiState.forecast?.type?.uppercase() ?: "",
                                    fontSize = 27.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    item {
                        CurrentDayForecastRow(
                            minTemp = forecastScreenUiState.currentDayForecastUiState.forecast?.min.toString(),
                            currentTemp = forecastScreenUiState.currentDayForecastUiState.forecast?.currentTemp.toString(),
                            maxTemp = forecastScreenUiState.currentDayForecastUiState.forecast?.max.toString()
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(2.dp)
                                .background(color = Color.White)
                        )
                    }

                    itemsIndexed(forecastScreenUiState.fiveDayForecastUiState.forecasts) { i, forecast ->
                        ForecastRow(
                            day = getNextFiveDays()[i],
                            weatherImage = getWeatherIcon(forecast.type),
                            weatherType = forecast.type,
                            maxTemp = forecast.max.toString()
                        )
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = forecastScreenUiState.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun ForecastRow(day: String, weatherImage: Int, weatherType: String, maxTemp: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(day, modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = weatherImage),
            contentDescription = weatherType + "Image",
            modifier = Modifier
                .size(30.dp)
                .weight(1f)
        )
        Text(
            maxTemp.plus(LocalContext.current.getString(R.string.degree_symbol)),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun CurrentDayForecastRow(minTemp: String, currentTemp: String, maxTemp: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val context = LocalContext.current
        Column {
            Text(minTemp + context.getString(R.string.degree_symbol))
            Text("min")
        }

        Column {
            Text(currentTemp.plus(context.getString(R.string.degree_symbol)))
            Text("Current")
        }

        Column {
            Text(maxTemp.plus(context.getString(R.string.degree_symbol)))
            Text("max")
        }
    }
}

@Composable
private fun BackgroundColor(weatherType: String?) = when {
    weatherType?.contains("rain", ignoreCase = true) == true -> Color(0xff57575D)
    weatherType?.contains("sun", ignoreCase = true) == true -> Color(0xff47AB2F)
    weatherType?.contains("cloud", ignoreCase = true) == true -> Color(0xff54717A)
    else -> Color(0xff54717A)
}

private fun getWeatherIcon(weatherType: String) = when {
    weatherType.contains("rain", ignoreCase = true) -> R.drawable.rain
    weatherType.contains("clear", ignoreCase = true) -> R.drawable.clear
    weatherType.contains("sun", ignoreCase = true) -> R.drawable.partlysunny
    weatherType.contains("cloud", ignoreCase = true) -> R.drawable.clear
    else -> R.drawable.partlysunny
}

private fun getBackgroundImage(weatherType: String?) = when {
    weatherType?.contains("rain", ignoreCase = true) == true -> R.drawable.forest_rainy
    weatherType?.contains("sun", ignoreCase = true) == true -> R.drawable.forest_sunny
    weatherType?.contains("cloud", ignoreCase = true) == true -> R.drawable.forest_cloudy
    else -> R.drawable.forest_cloudy
}
