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

package com.example.android.interactivesliceprovider.slicebuilders

import android.content.Context
import android.net.Uri
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.SliceAction
import androidx.slice.builders.cell
import androidx.slice.builders.gridRow
import androidx.slice.builders.list
import androidx.slice.builders.row
import com.example.android.interactivesliceprovider.InteractiveSliceProvider
import com.example.android.interactivesliceprovider.MyBroadcastReceiver
import com.example.android.interactivesliceprovider.R
import com.example.android.interactivesliceprovider.R.drawable
import com.example.android.interactivesliceprovider.SliceBuilder

class GallerySliceBuilder(
    val context: Context,
    val sliceUri: Uri
) : SliceBuilder {

    override fun buildSlice() = list(context, sliceUri, ListBuilder.INFINITY) {
        val primaryAction = SliceAction(
            MyBroadcastReceiver.getIntent(
                context, InteractiveSliceProvider.ACTION_TOAST, "open photo album"
            ),
            IconCompat.createWithResource(context, drawable.slices_1),
            ListBuilder.LARGE_IMAGE,
            "Open photo album"
        )
        return list(context, sliceUri, ListBuilder.INFINITY) {
            setAccentColor(0xff4285)
            row {
                setTitle("Family trip to Hawaii")
                setSubtitle("Sep 30, 2017 - Oct 2, 2017")
                setPrimaryAction(primaryAction)
            }
            addAction(
                SliceAction(
                    MyBroadcastReceiver.getIntent(
                        context,
                        InteractiveSliceProvider.ACTION_TOAST, "cast photo album"
                    ),
                    IconCompat.createWithResource(context, drawable.ic_cast), "Cast photo album"
                )
            )
            addAction(
                SliceAction(
                    MyBroadcastReceiver.getIntent(
                        context,
                        InteractiveSliceProvider.ACTION_TOAST, "share photo album"
                    ),
                    IconCompat.createWithResource(context, drawable.ic_share),
                    "Share photo album"
                )
            )
            gridRow {
                val galleryResId = intArrayOf(
                    R.drawable.slices_1, R.drawable.slices_2, R.drawable.slices_3,
                    R.drawable.slices_4
                )
                val imageCount = 7
                for (i in 0 until imageCount) {
                    val ic = IconCompat.createWithResource(
                        context, galleryResId[i % galleryResId.size]
                    )
                    cell {
                        addImage(ic, ListBuilder.LARGE_IMAGE)
                    }
                }
                setPrimaryAction(primaryAction)
                setSeeMoreAction(
                    MyBroadcastReceiver.getIntent(
                        context,
                        InteractiveSliceProvider.ACTION_TOAST, "see your gallery"
                    )
                )
                setContentDescription("Images from your trip to Hawaii")
            }
        }
    }

    companion object {
        const val TAG = "GallerySliceBuilder"
    }
}