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

import android.net.Uri
import androidx.slice.Slice
import com.example.android.interactivesliceprovider.data.model.AppIndexingMetadata
import com.google.firebase.appindexing.FirebaseAppIndex
import com.google.firebase.appindexing.Indexable

abstract class SliceBuilder(val sliceUri: Uri, val appIndexingMetadata: AppIndexingMetadata) {
    abstract fun buildSlice(): Slice
    fun updateAppIndex() {
        FirebaseAppIndex.getInstance()
            .update(
                Indexable.Builder()
                    .setUrl(appIndexingMetadata.url)
                    .setName(appIndexingMetadata.name)
                    .setKeywords(*appIndexingMetadata.keywords.toTypedArray())
                    .setMetadata(
                        Indexable.Metadata.Builder().setSliceUri(sliceUri)
                    )
                    .build()
            )
    }
}