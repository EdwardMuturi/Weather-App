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
fun ForecastScreen(latitude: Double, longitude: Double, forecastViewModel: ForecastViewModel = viewModel()) {
    val fiveDayForecastUiState by forecastViewModel.fiveDayForecastUiState
    val currentDayForecastUiState by forecastViewModel.currentForecastUiState
    val context = LocalContext.current
    val pullRefreshState = rememberPullRefreshState(refreshing = currentDayForecastUiState.isLoading, onRefresh = {
        forecastViewModel.loadCurrentDayForecast(
            latitude = latitude,
            longitude = longitude
        )
    })

    LaunchedEffect(key1 = currentDayForecastUiState.isLoading, block = {
        forecastViewModel.loadCurrentDayForecast(
            latitude = latitude,
            longitude = longitude
        )
        forecastViewModel.loadFiveDayForecast(
            latitude = latitude,
            longitude = longitude
        )
    })

    Scaffold(containerColor = Color(0xff54717A)) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
                .pullRefresh(pullRefreshState)
        ) {
            if (!currentDayForecastUiState.isLoading) {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .testTag("FiveDayForecastColumn")
                ) {
                    stickyHeader {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Image(
                                painter = painterResource(id = R.drawable.forest_cloudy),
                                contentDescription = stringResource(R.string.text_weather_type_image),
                                modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
                                contentScale = ContentScale.Crop
                            )
                            Column(Modifier.align(Alignment.Center)) {
                                Text(
                                    text = currentDayForecastUiState.forecast?.currentTemp.toString()
                                        .plus(context.getString(R.string.degree_symbol)),
                                    fontSize = 52.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 50.dp)
                                )

                                Text(
                                    text = currentDayForecastUiState.forecast?.type?.uppercase() ?: "",
                                    fontSize = 27.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    item {
                        CurrentDayForecastRow(
                            minTemp = currentDayForecastUiState.forecast?.min.toString(),
                            currentTemp = currentDayForecastUiState.forecast?.currentTemp.toString(),
                            maxTemp = currentDayForecastUiState.forecast?.max.toString()
                        )
                        Spacer(modifier = Modifier.fillMaxWidth().size(2.dp).background(color = Color.White))
                    }

                    itemsIndexed(fiveDayForecastUiState.forecasts.take(5)) { i, forecast ->
                        ForecastRow(
                            day = getNextFiveDays()[i],
                            weatherImage = R.drawable.rain,
                            weatherType = forecast.type,
                            maxTemp = forecast.max.toString()
                        )
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = currentDayForecastUiState.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun ForecastRow(day: String, weatherImage: Int, weatherType: String, maxTemp: String) {
    Row(
        Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(day, modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = weatherImage),
            contentDescription = weatherType + "Image",
            modifier = Modifier.size(30.dp).weight(1f)
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
        Modifier.fillMaxWidth()
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
