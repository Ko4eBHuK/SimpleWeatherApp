package com.okladnikov.yadroweather.utils

import java.time.LocalDate

fun String.toDayOfTheWeek() = LocalDate.parse(this).getDayOfWeek().name
