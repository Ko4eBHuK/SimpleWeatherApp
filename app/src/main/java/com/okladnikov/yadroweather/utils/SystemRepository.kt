package com.okladnikov.yadroweather.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.icu.util.TimeZone
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationRequestCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SystemRepository @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
) {
    fun getLocationFused(
        onSuccess: (Location) -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onFailure("Location permission not granted")
        } else {
            fusedLocationClient.getCurrentLocation(
                LocationRequestCompat.QUALITY_LOW_POWER,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                        CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                }
            ).addOnFailureListener {
                onFailure("Fail to determine location. ${it.message}")
            }.addOnSuccessListener { location: Location? ->
                if (location == null) {
                    onFailure("Location not recognized.")
                } else {
                    onSuccess(location)
                }
            }
        }
    }

    fun getTimeZone() = TimeZone.getDefault().id
}