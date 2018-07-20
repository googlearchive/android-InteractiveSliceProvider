/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.interactivesliceprovider

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.util.Log
import androidx.core.content.edit
import androidx.slice.Slice
import androidx.slice.SliceManager
import androidx.slice.SliceProvider
import com.example.android.interactivesliceprovider.Paths.GALLERY
import com.example.android.interactivesliceprovider.Paths.HELLO
import com.example.android.interactivesliceprovider.Paths.INPUT_RANGE
import com.example.android.interactivesliceprovider.Paths.LOAD_GRID
import com.example.android.interactivesliceprovider.Paths.LOAD_LIST
import com.example.android.interactivesliceprovider.Paths.NOTE
import com.example.android.interactivesliceprovider.Paths.RANGE
import com.example.android.interactivesliceprovider.Paths.RESERVATION
import com.example.android.interactivesliceprovider.Paths.RIDE
import com.example.android.interactivesliceprovider.Paths.TOGGLE
import com.example.android.interactivesliceprovider.Paths.WEATHER
import com.example.android.interactivesliceprovider.Paths.WIFI
import com.example.android.interactivesliceprovider.data.DataRepository
import com.example.android.interactivesliceprovider.data.FakeDataSource
import com.example.android.interactivesliceprovider.data.model.AppIndexingMetadata
import com.example.android.interactivesliceprovider.slicebuilders.GallerySliceBuilder
import com.example.android.interactivesliceprovider.slicebuilders.GridSliceBuilder
import com.example.android.interactivesliceprovider.slicebuilders.HelloSliceBuilder
import com.example.android.interactivesliceprovider.slicebuilders.InputRangeSliceBuilder
import com.example.android.interactivesliceprovider.slicebuilders.ListSliceBuilder
import com.example.android.interactivesliceprovider.slicebuilders.NoteSliceBuilder
import com.example.android.interactivesliceprovider.slicebuilders.RangeSliceBuilder
import com.example.android.interactivesliceprovider.slicebuilders.ReservationSliceBuilder
import com.example.android.interactivesliceprovider.slicebuilders.RideSliceBuilder
import com.example.android.interactivesliceprovider.slicebuilders.ToggleSliceBuilder
import com.example.android.interactivesliceprovider.slicebuilders.WeatherSliceBuilder
import com.example.android.interactivesliceprovider.slicebuilders.WifiSliceBuilder
import com.example.android.interactivesliceprovider.util.LazyFunctionMap
import com.example.android.interactivesliceprovider.util.buildUriWithAuthority

/**
 * Examples of using slice template builders.
 */
class InteractiveSliceProvider : SliceProvider() {

    private lateinit var repo: DataRepository
    private lateinit var contentNotifiers: LazyFunctionMap<Uri, Unit>

    override fun onCreateSliceProvider(): Boolean {
        repo = DataRepository(FakeDataSource(Handler()))
        contentNotifiers = LazyFunctionMap {
            context.contentResolver.notifyChange(it, null)
        }
        return true
    }

    override fun onMapIntentToUri(intent: Intent): Uri {
        return context.buildUriWithAuthority(intent.data.path.replace("/", ""))
    }

    override fun onBindSlice(sliceUri: Uri?): Slice? {
        if (sliceUri == null || sliceUri.path == null) {
            return null
        }
        val sliceBuilder = getSliceBuilder(sliceUri)
        return getSliceBuilder(sliceUri)?.apply { updateAppIndex() }?.buildSlice()
    }

    private fun getSliceBuilder(sliceUri: Uri) = when (sliceUri.path) {
        HELLO -> HelloSliceBuilder(
            context = context,
            sliceUri = sliceUri,
            appIndexingMetaData = AppIndexingMetadata(
                url = "https://interactivesliceprovider.android.example.com$WIFI",
                name = "Wifi",
                keywords = listOf("wifi")
            )
        )
        WIFI -> WifiSliceBuilder(
            context = context,
            sliceUri = sliceUri,
            appIndexingMetaData = AppIndexingMetadata(
                url = "https://interactivesliceprovider.android.example.com$WIFI",
                name = "Wifi",
                keywords = listOf("wifi")
            )
        )
        NOTE -> NoteSliceBuilder(
            context = context,
            sliceUri = sliceUri,
            appIndexingMetaData = AppIndexingMetadata(
                url = "https://interactivesliceprovider.android.example.com$NOTE",
                name = "Note",
                keywords = listOf("note")
            )
        )
        RIDE -> RideSliceBuilder(
            context = context,
            sliceUri = sliceUri,
            appIndexingMetaData = AppIndexingMetadata(
                url = "https://interactivesliceprovider.android.example.com$RIDE",
                name = "Ride",
                keywords = listOf("ride")
            )
        )
        TOGGLE -> ToggleSliceBuilder(
            context = context,
            sliceUri = sliceUri,
            appIndexingMetaData = AppIndexingMetadata(
                url = "https://interactivesliceprovider.android.example.com$TOGGLE",
                name = "Toggle",
                keywords = listOf("toggle")
            )
        )
        GALLERY -> GallerySliceBuilder(
            context = context,
            sliceUri = sliceUri,
            appIndexingMetaData = AppIndexingMetadata(
                url = "https://interactivesliceprovider.android.example.com$GALLERY",
                name = "Gallery",
                keywords = listOf("gallery")
            )
        )
        WEATHER -> WeatherSliceBuilder(
            context = context,
            sliceUri = sliceUri,
            appIndexingMetaData = AppIndexingMetadata(
                url = "https://interactivesliceprovider.android.example.com$WEATHER",
                name = "Weather",
                keywords = listOf("weather")
            )
        )
        RESERVATION -> ReservationSliceBuilder(
            context = context,
            sliceUri = sliceUri,
            appIndexingMetaData = AppIndexingMetadata(
                url = "https://interactivesliceprovider.android.example.com$RESERVATION",
                name = "Reservation",
                keywords = listOf("reservation")
            )
        )
        LOAD_LIST -> ListSliceBuilder(
            context = context,
            sliceUri = sliceUri,
            repo = repo,
            appIndexingMetaData = AppIndexingMetadata(
                url = "https://interactivesliceprovider.android.example.com$LOAD_LIST",
                name = "Load List",
                keywords = listOf("load", "list")
            )
        )
        LOAD_GRID -> GridSliceBuilder(
            context = context,
            sliceUri = sliceUri,
            repo = repo,
            appIndexingMetaData = AppIndexingMetadata(
                url = "https://interactivesliceprovider.android.example.com$LOAD_GRID",
                name = "Load Grid",
                keywords = listOf("load", "grid")
            )
        )
        INPUT_RANGE -> InputRangeSliceBuilder(
            context = context,
            sliceUri = sliceUri,
            appIndexingMetaData = AppIndexingMetadata(
                url = "https://interactivesliceprovider.android.example.com$INPUT_RANGE",
                name = "Input Range",
                keywords = listOf("input", "range")
            )
        )
        RANGE -> RangeSliceBuilder(
            context = context,
            sliceUri = sliceUri,
            appIndexingMetaData = AppIndexingMetadata(
                url = "https://interactivesliceprovider.android.example.com$RANGE",
                name = "Range",
                keywords = listOf("range")
            )
        )
        else -> {
            Log.e(TAG, "Unknown URI: $sliceUri")
            null
        }
    }

    override fun onSlicePinned(sliceUri: Uri?) {
        super.onSlicePinned(sliceUri)
        Log.d(TAG, "onSlicePinned - ${sliceUri?.path}")
        when (sliceUri?.path) {
            LOAD_LIST -> repo.registerListSliceDataCallback(contentNotifiers[sliceUri])
            LOAD_GRID -> repo.registerGridSliceDataCallback(contentNotifiers[sliceUri])
            else -> Log.e(TAG, "Unknown URI: $sliceUri")
        }
    }

    override fun onSliceUnpinned(sliceUri: Uri?) {
        super.onSliceUnpinned(sliceUri)
        Log.d(TAG, "onSliceUnpinned - ${sliceUri?.path}")
        when (sliceUri?.path) {
            LOAD_LIST -> repo.unregisterListSliceDataCallbacks()
            LOAD_GRID -> repo.unregisterGridSliceDataCallbacks()
            else -> Log.e(TAG, "Unknown URI: $sliceUri")
        }
    }

    companion object {
        const val TAG = "SliceProvider"
        const val ACTION_WIFI_CHANGED = "com.example.androidx.slice.action.WIFI_CHANGED"
        const val ACTION_TOAST = "com.example.androidx.slice.action.TOAST"
        const val EXTRA_TOAST_MESSAGE = "com.example.androidx.extra.TOAST_MESSAGE"
        const val ACTION_TOAST_RANGE_VALUE = "com.example.androidx.slice.action.TOAST_RANGE_VALUE"

        fun getPendingIntent(context: Context, action: String): PendingIntent {
            val intent = Intent(action)
            return PendingIntent.getActivity(context, 0, intent, 0)
        }
    }
}

object Paths {
    const val HELLO = "/hello"
    const val WIFI = "/wifi"
    const val NOTE = "/note"
    const val RIDE = "/ride"
    const val TOGGLE = "/toggle"
    const val GALLERY = "/gallery"
    const val WEATHER = "/weather"
    const val RESERVATION = "/reservation"
    const val LOAD_LIST = "/loadlist"
    const val LOAD_GRID = "/loadgrid"
    const val INPUT_RANGE = "/inputrange"
    const val RANGE = "/range"
}