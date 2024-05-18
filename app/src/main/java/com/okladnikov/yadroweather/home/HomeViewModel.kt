package com.okladnikov.yadroweather.home

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okladnikov.yadroweather.home.screen.HomeScreenIntent
import com.okladnikov.yadroweather.home.screen.HomeScreenState
import com.okladnikov.yadroweather.utils.Status
import com.okladnikov.yadroweather.utils.toDayOfTheWeek
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {
    private val _screenStateFlow = MutableStateFlow(HomeScreenState())
    val screenStateFlow: StateFlow<HomeScreenState> = _screenStateFlow

    init {
        updateData()
    }

    fun processIntent(intent: HomeScreenIntent) {
        when (intent) {
            HomeScreenIntent.Refresh -> updateData()
            HomeScreenIntent.CloseError -> {
                _screenStateFlow.value = _screenStateFlow.value.copy(isError = false)
            }
        }
    }

    private fun updateData() {
        viewModelScope.launch(Dispatchers.IO) {
            _screenStateFlow.value = _screenStateFlow.value.copy(isLoading = true)

            val onLocationSuccess: (Location) -> Unit = {
                _screenStateFlow.value = _screenStateFlow.value.copy(isLoading = false)

                getLocationName(it.latitude, it.longitude)
                getCurrentConditions(it.latitude, it.longitude)
                getWeeklyForecast(it.latitude, it.longitude)
            }
            val onLocationFailure: (String) -> Unit = {
                _screenStateFlow.value = _screenStateFlow.value.copy(
                    isError = true,
                    isLoading = false,
                    message = it
                )
            }

            repository.getLocation(onLocationSuccess, onLocationFailure)
        }
    }

    private fun getLocationName(latitude: Double, longitude: Double) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadLocationName(latitude, longitude).collect {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data?.city != null) {
                            val locality = it.data.locality ?: ""
                            _screenStateFlow.value = _screenStateFlow.value.copy(
                                isLoading = false,
                                locationInfo = it.data.city + ", " + locality
                            )
                        } else {
                            _screenStateFlow.value = _screenStateFlow.value.copy(
                                isError = true,
                                isLoading = false,
                                message = "Unable to get location name"
                            )
                        }
                    }
                    Status.ERROR -> {
                        _screenStateFlow.value = _screenStateFlow.value.copy(
                            isLoading = false,
                            isError = true,
                            locationInfo = it.message,
                        )
                    }
                    Status.LOADING -> {
                        _screenStateFlow.value = _screenStateFlow.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }

    private fun getCurrentConditions(latitude: Double, longitude: Double) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadCurrentWeatherForLocation(latitude, longitude).collect {
                when (it.status) {
                    Status.SUCCESS -> {
                        _screenStateFlow.value = _screenStateFlow.value.copy(
                            isLoading = false,
                            temperature = if (it.data?.conditions?.temperature != null)
                                "${it.data.conditions.temperature.roundToInt()}\u2103"
                                else "Unavailable",
                        )
                    }
                    Status.ERROR -> {
                        _screenStateFlow.value = _screenStateFlow.value.copy(
                            isLoading = false,
                            isError = true,
                            locationInfo = it.message,
                        )
                    }
                    Status.LOADING -> {
                        _screenStateFlow.value = _screenStateFlow.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }

    private fun getWeeklyForecast(latitude: Double, longitude: Double) = viewModelScope.launch(Dispatchers.IO) {
        repository.loadWeeklyForecastForLocation(latitude, longitude).collect { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    if (
                        it.data?.days?.time != null
                        && it.data.days.temperatureMaxList != null
                        && it.data.days.temperatureMinList != null
                        && it.data.days.time.size == it.data.days.temperatureMaxList.size
                        && it.data.days.time.size == it.data.days.temperatureMinList.size
                    ) {
                        val weekInfo = it.data.days.time
                            .zip(it.data.days.temperatureMaxList.map { maxTemp -> maxTemp.roundToInt()})
                            .zip(it.data.days.temperatureMinList.map { minTemp -> minTemp.roundToInt()})
                            .mapIndexed { index, data ->
                                val dayName = if (index == 0) "Today"
                                else data.first.first.toDayOfTheWeek().lowercase()
                                    .replaceFirstChar { first -> first.titlecase() }

                                HomeScreenState.DayInfo(
                                    name = dayName,
                                    temperatureMax = "${data.first.second}\u2103",
                                    temperatureMin = "${data.second}\u2103"
                                )
                            }

                        _screenStateFlow.value = _screenStateFlow.value.copy(
                            isLoading = false,
                            weeklyForecast = weekInfo
                        )
                    } else {
                        _screenStateFlow.value = _screenStateFlow.value.copy(
                            isLoading = false,
                            isError = true,
                            locationInfo = "Weekly data corrupted"
                        )
                    }
                }
                Status.ERROR -> {
                    _screenStateFlow.value = _screenStateFlow.value.copy(
                        isLoading = false,
                        isError = true,
                        locationInfo = it.message
                    )
                }
                Status.LOADING -> {
                    _screenStateFlow.value = _screenStateFlow.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }
}
