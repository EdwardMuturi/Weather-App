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
package com.edwardmuturi.location.presentation.getlocationinfo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import timber.log.Timber

@Composable
fun LocationScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    locationViewModel: LocationViewModel = viewModel(),
    onLocationUpdated: (LocationDetails) -> Unit
) {
    val context = LocalContext.current
    var locationRequired = false
    var locationCallback: LocationCallback? = null
    var fusedLocationClient: FusedLocationProviderClient? = null
    var currentLocation by remember { mutableStateOf(LocationDetails(0.toDouble(), 0.toDouble())) }

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            for (location in p0.locations) {
                currentLocation = LocationDetails(location.latitude, location.longitude)
                onLocationUpdated(currentLocation)
                Timber.e("sent location $currentLocation")
            }
            if (p0.locations.isNotEmpty()) {
                locationViewModel.saveCurrentLocation(currentLocation)
            }
        }
    }

    val launcherMultiplePermissions = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            locationRequired = true
            startLocationUpdates(locationRequired = locationRequired, locationCallback = locationCallback, fusedLocationClient = fusedLocationClient)
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    if (permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    ) {
        startLocationUpdates(locationRequired = locationRequired, locationCallback = locationCallback, fusedLocationClient = fusedLocationClient)
    } else {
        LaunchedEffect(key1 = true, block = {
            launcherMultiplePermissions.launch(permissions)
        })
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    Timber.d("onCreate")
                }

                Lifecycle.Event.ON_START -> {
                    Timber.d("On Start")
                }

                Lifecycle.Event.ON_RESUME -> {
                    Timber.d("On Resume")
                    startLocationUpdates(locationRequired, locationCallback, fusedLocationClient)
                }

                Lifecycle.Event.ON_PAUSE -> {
                    Timber.d("On Pause")
                    removeLocationUpdates(locationCallback = locationCallback, fusedLocationClient = fusedLocationClient)
                }

                Lifecycle.Event.ON_STOP -> {
                    Timber.d("On Stop")
                }

                Lifecycle.Event.ON_DESTROY -> {
                    Timber.d("On Destroy")
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@SuppressLint("MissingPermission")
private fun startLocationUpdates(locationRequired: Boolean, locationCallback: LocationCallback?, fusedLocationClient: FusedLocationProviderClient?) {
    if (locationRequired) {
        locationCallback?.let {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationClient?.requestLocationUpdates(locationRequest, it, Looper.getMainLooper())
        }
    }
}

fun removeLocationUpdates(locationCallback: LocationCallback?, fusedLocationClient: FusedLocationProviderClient?) {
    locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
}

data class LocationDetails(val latitude: Double, val longitude: Double)
