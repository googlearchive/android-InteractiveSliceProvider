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

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.util.Log
import androidx.slice.Slice
import androidx.slice.SliceProvider
import com.example.android.interactivesliceprovider.Paths.GALLERY
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
import com.example.android.interactivesliceprovider.slicebuilders.*
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
        return context.buildUriWithAuthority(intent.data.path)
    }

    override fun onBindSlice(sliceUri: Uri?): Slice? {
        if (sliceUri == null || sliceUri.path == null) {
            return null
        }
        return getSliceBuilder(sliceUri)?.buildSlice()
    }

    private fun getSliceBuilder(sliceUri: Uri) = when (sliceUri.path) {
        WIFI -> WifiSliceBuilder(context, sliceUri)
        NOTE -> NoteSliceBuilder(context, sliceUri)
        RIDE -> RideSliceBuilder(context, sliceUri)
        TOGGLE -> ToggleSliceBuilder(context, sliceUri)
        GALLERY -> GallerySliceBuilder(context, sliceUri)
        WEATHER -> WeatherSliceBuilder(context, sliceUri)
        RESERVATION -> ReservationSliceBuilder(context, sliceUri)
        LOAD_LIST -> ListSliceBuilder(context, sliceUri, repo)
        LOAD_GRID -> GridSliceBuilder(context, sliceUri, repo)
        INPUT_RANGE -> InputRangeSliceBuilder(context, sliceUri)
        RANGE -> RangeSliceBuilder(context, sliceUri)
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
    }
}

object Paths {
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