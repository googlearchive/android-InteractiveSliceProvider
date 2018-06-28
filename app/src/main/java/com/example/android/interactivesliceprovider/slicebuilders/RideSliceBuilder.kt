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
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.builders.SliceAction
import androidx.slice.builders.header
import androidx.slice.builders.list
import androidx.slice.builders.row
import com.example.android.interactivesliceprovider.InteractiveSliceProvider
import com.example.android.interactivesliceprovider.MyBroadcastReceiver
import com.example.android.interactivesliceprovider.R.drawable
import com.example.android.interactivesliceprovider.SliceBuilder
import java.util.concurrent.TimeUnit

class RideSliceBuilder(
    val context: Context,
    val sliceUri: Uri
) : SliceBuilder {

    override fun buildSlice(): Slice {
        val colorSpan = ForegroundColorSpan(-0xf062a8)
        val headerSubtitle = SpannableString("Ride in 4 min").apply {
            setSpan(
                colorSpan, 8, length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        val homeSubtitle = SpannableString("12 miles | 12 min | $9.00").apply {
            setSpan(
                colorSpan, 20, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        val workSubtitle = SpannableString("44 miles | 1 hour 45 min | $31.41").apply {
            setSpan(
                colorSpan, 27, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        val primaryAction = SliceAction(
            MyBroadcastReceiver.getIntent(
                context, InteractiveSliceProvider.ACTION_TOAST, "get ride"
            ),
            IconCompat.createWithResource(context, drawable.ic_car), "Get Ride"
        )
        return list(context, sliceUri, TimeUnit.SECONDS.toMillis(10)) {
            setAccentColor(0xff4285)
            header {
                setTitle("Get ride")
                setSubtitle(headerSubtitle)
                setSummary("Ride to work in 12 min | Ride home in 1 hour 45 min")
                setPrimaryAction(primaryAction)
            }
            row {
                setTitle("Work")
                setSubtitle(workSubtitle)
                addEndItem(
                    SliceAction(
                        MyBroadcastReceiver.getIntent(
                            context, InteractiveSliceProvider.ACTION_TOAST, "work"
                        ),
                        IconCompat.createWithResource(context, drawable.ic_work),
                        "Get ride to work"
                    )
                )
            }
            row {
                setTitle("Home")
                setSubtitle(homeSubtitle)
                addEndItem(
                    SliceAction(
                        MyBroadcastReceiver.getIntent(
                            context, InteractiveSliceProvider.ACTION_TOAST, "home"
                        ),
                        IconCompat.createWithResource(context, drawable.ic_home),
                        "Get ride home"
                    )
                )
            }
        }
    }

    companion object {
        const val TAG = "ListSliceBuilder"
    }
}