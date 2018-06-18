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
import androidx.slice.Slice
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.SliceAction
import androidx.slice.builders.list
import androidx.slice.builders.range
import com.example.android.interactivesliceprovider.InteractiveSliceProvider
import com.example.android.interactivesliceprovider.MyBroadcastReceiver
import com.example.android.interactivesliceprovider.R
import com.example.android.interactivesliceprovider.SliceBuilder

class RangeSliceBuilder(
    val context: Context,
    val sliceUri: Uri
) : SliceBuilder {

    override fun buildSlice(): Slice {
        val icon = IconCompat.createWithResource(context, R.drawable.ic_star_on)
        val primaryAction = SliceAction(
            MyBroadcastReceiver.getIntent(
                context, InteractiveSliceProvider.ACTION_TOAST, "open download"
            ), icon,
            "Download"
        )
        return list(context, sliceUri, ListBuilder.INFINITY) {
            setAccentColor(0xff4285)
            range {
                setTitle("Download progress")
                setSubtitle("Download is happening")
                setMax(100)
                setValue(75)
                setPrimaryAction(primaryAction)
            }
        }
    }

    companion object {
        const val TAG = "ListSliceBuilder"
    }
}