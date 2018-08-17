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
                .addOnCompleteListener { Log.v(TAG, "App Indexing complete") }
                .addOnFailureListener { Log.v(TAG, "App Indexing failed: $it") }
    }

    /*
     * Retrieves full list of indexable data for each slice. Here is an example of what is included
     * for each item:
     *      url = https://interactivesliceprovider.android.example.com/wifi
     *      name = Wifi
     *      keywords = wifi
     */
    private fun getIndexableData(): List<AppIndexingMetadata> {
        return listOf(
                AppIndexingMetadata(
                        url = HOST + Paths.DEFAULT,
                        name = "Default",
                        keywords = listOf("default")
                ),
                AppIndexingMetadata(
                        url = HOST + Paths.HELLO,
                        name = "Hello",
                        keywords = listOf("hello")
                ),
                AppIndexingMetadata(
                        url = HOST + Paths.WIFI,
                        name = "Wifi",
                        keywords = listOf("wifi")
                ),
                AppIndexingMetadata(
                        url = HOST + Paths.NOTE,
                        name = "Note",
                        keywords = listOf("note")
                ),
                AppIndexingMetadata(
                        url = HOST + Paths.RIDE,
                        name = "Ride",
                        keywords = listOf("ride")
                ),
                AppIndexingMetadata(
                        url = HOST + Paths.TOGGLE,
                        name = "Toggle",
                        keywords = listOf("toggle")
                ),
                AppIndexingMetadata(
                        url = HOST + Paths.GALLERY,
                        name = "Gallery",
                        keywords = listOf("gallery")
                ),
                AppIndexingMetadata(
                        url = HOST + Paths.WEATHER,
                        name = "Weather",
                        keywords = listOf("weather")
                ),
                AppIndexingMetadata(
                        url = HOST + Paths.RESERVATION,
                        name = "Reservation",
                        keywords = listOf("reservation")
                ),
                AppIndexingMetadata(
                        url = HOST + Paths.LOAD_LIST,
                        name = "Load List",
                        keywords = listOf("load", "list")
                ),
                AppIndexingMetadata(
                        url = HOST + Paths.LOAD_GRID,
                        name = "Load Grid",
                        keywords = listOf("load", "grid")
                ),
                AppIndexingMetadata(
                        url = HOST + Paths.INPUT_RANGE,
                        name = "Input Range",
                        keywords = listOf("input", "range")
                ),
                AppIndexingMetadata(
                        url = HOST + Paths.RANGE,
                        name = "Range",
                        keywords = listOf("range")
                )
        )
    }
}