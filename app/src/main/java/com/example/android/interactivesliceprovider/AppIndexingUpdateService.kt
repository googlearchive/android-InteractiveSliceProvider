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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.JobIntentService
import com.example.android.interactivesliceprovider.data.model.AppIndexingMetadata
import com.google.firebase.appindexing.FirebaseAppIndex
import com.google.firebase.appindexing.Indexable

class AppIndexingUpdateService : JobIntentService() {

    private val TAG = AppIndexingUpdateService::class.java.simpleName

    companion object {
        private const val UNIQUE_JOB_ID = 42
        fun enqueueWork(context: Context) {
            enqueueWork(context, AppIndexingUpdateService::class.java, UNIQUE_JOB_ID, Intent())
        }
    }

    override fun onHandleWork(intent: Intent) {

        // Retrieve list of indexable data for each slice.
        val sliceIndexableDataList = getIndexableData()

        // Convert list of AppIndexingMetadata objects (custom class) to a list of Indexable
        // objects, so FirebaseAppIndex can consume them.
        val firebaseAppIndex = FirebaseAppIndex.getInstance()
        val appIndexDataList = mutableListOf<Indexable>()

        for(indexableData in sliceIndexableDataList) {
            appIndexDataList.add(
                    Indexable.Builder()
                            .setUrl(indexableData.url)
                            .setName(indexableData.name)
                            .setKeywords(*indexableData.keywords.toTypedArray())
                            .setMetadata(
                                    Indexable.Metadata.Builder()
                                            .setSliceUri(Uri.parse(indexableData.url)))
                            .build()
            )
        }

        firebaseAppIndex.update(*appIndexDataList.toTypedArray())
                .addOnSuccessListener { Log.d(TAG, "App Indexing succeeded.") }
                .addOnFailureListener { Log.d(TAG, "App Indexing failed: $it") }
    }

    /*
     * Retrieves full list of indexable data for each slice. Here is an example of what is included
     * for each item:
     *      url = https://interactivesliceprovider.android.example.com/wifi
     *      name = Wifi
     *      keywords = wifi
     */
    private fun getIndexableData(): List<AppIndexingMetadata> {

        // Retrieve data for each Indexable type (slices for this sample).

        // TODO: Move all data for Indexables to the data/domain layer.

        // The resource string for scheme doesn't include "://" because android:scheme for the
        // data element in the manifest doesn't allow it. Therefore, we must add it here to create
        // the complete URL.
        val schemeAndHost = "${application.resources.getString(R.string.scheme_slice_url)}://" +
                "${applicationContext.resources.getString(R.string.host_slice_url)}"

        val default_path = application.resources.getString(R.string.default_slice_path)

        val hello_path = application.resources.getString(R.string.hello_slice_path)
        val wifi_path = application.resources.getString(R.string.wifi_slice_path)
        val note_path = application.resources.getString(R.string.note_slice_path)

        val ride_path = application.resources.getString(R.string.ride_slice_path)
        val toggle_path = application.resources.getString(R.string.toggle_slice_path)
        val gallery_path = application.resources.getString(R.string.gallery_slice_path)

        val weather_path = application.resources.getString(R.string.weather_slice_path)
        val reservation_path = application.resources.getString(R.string.reservation_slice_path)
        val load_list_path = application.resources.getString(R.string.list_slice_path)

        val load_grid_path = application.resources.getString(R.string.grid_slice_path)
        val input_range_path = application.resources.getString(R.string.input_slice_path)
        val range_path = application.resources.getString(R.string.range_slice_path)

        return listOf(
                AppIndexingMetadata(
                        url = schemeAndHost + default_path,
                        name = "Default",
                        keywords = listOf("default", "defaultest1234")
                ),
                AppIndexingMetadata(
                        url = schemeAndHost + hello_path,
                        name = "Hello",
                        keywords = listOf("hello", "hellotest1234")
                ),
                AppIndexingMetadata(
                        url = schemeAndHost + wifi_path,
                        name = "Wifi",
                        keywords = listOf("wifi", "wifitest1234")
                ),
                AppIndexingMetadata(
                        url = schemeAndHost + note_path,
                        name = "Note",
                        keywords = listOf("note", "notetest1234")
                ),
                AppIndexingMetadata(
                        url = schemeAndHost + ride_path,
                        name = "Ride",
                        keywords = listOf("ride", "ridetest1234")
                ),
                AppIndexingMetadata(
                        url = schemeAndHost + toggle_path,
                        name = "Toggle",
                        keywords = listOf("toggle", "toggletest1234")
                ),
                AppIndexingMetadata(
                        url = schemeAndHost + gallery_path,
                        name = "Gallery",
                        keywords = listOf("gallery", "gallerytest1234")
                ),
                AppIndexingMetadata(
                        url = schemeAndHost + weather_path,
                        name = "Weather",
                        keywords = listOf("weather", "weathertest1234")
                ),
                AppIndexingMetadata(
                        url = schemeAndHost + reservation_path,
                        name = "Reservation",
                        keywords = listOf("reservation", "reservationtest1234")
                ),
                AppIndexingMetadata(
                        url = schemeAndHost + load_list_path,
                        name = "Load List",
                        keywords = listOf("load", "list", "loadlist", "loadlisttest1234")
                ),
                AppIndexingMetadata(
                        url = schemeAndHost + load_grid_path,
                        name = "Load Grid",
                        keywords = listOf("load", "grid", "loadgrid", "loadgridtest1234")
                ),
                AppIndexingMetadata(
                        url = schemeAndHost + input_range_path,
                        name = "Input Range",
                        keywords = listOf("input", "range", "inputrange", "inputrangetest1234")
                ),
                AppIndexingMetadata(
                        url = schemeAndHost + range_path,
                        name = "Range",
                        keywords = listOf("range", "rangetest1234")
                )
        )
    }
}