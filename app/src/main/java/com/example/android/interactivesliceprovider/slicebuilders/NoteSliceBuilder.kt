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
import androidx.slice.builders.list
import androidx.slice.builders.row
import com.example.android.interactivesliceprovider.InteractiveSliceProvider
import com.example.android.interactivesliceprovider.MainActivity
import com.example.android.interactivesliceprovider.MyBroadcastReceiver
import com.example.android.interactivesliceprovider.R.drawable
import com.example.android.interactivesliceprovider.SliceBuilder

class NoteSliceBuilder(
    val context: Context,
    val sliceUri: Uri
) : SliceBuilder {

    override fun buildSlice() = list(context, sliceUri, ListBuilder.INFINITY) {
        setAccentColor(0xff4285)
        row { setTitle("Create new note") }
        addAction(
            SliceAction.create(
                MyBroadcastReceiver.getIntent(
                    context, InteractiveSliceProvider.ACTION_TOAST, "create note"
                ),
                IconCompat.createWithResource(context, drawable.ic_create),
                ListBuilder.ICON_IMAGE,
                "Create note"
            )
        )
        addAction(
            SliceAction.create(
                MyBroadcastReceiver.getIntent(
                    context, InteractiveSliceProvider.ACTION_TOAST, "voice note"
                ),
                IconCompat.createWithResource(context, drawable.ic_voice),
                ListBuilder.ICON_IMAGE,
                "Voice note"
            )
        )
        addAction(
            SliceAction.create(
                MainActivity.getIntent(context, "android.media.action.IMAGE_CAPTURE"),
                IconCompat.createWithResource(context, drawable.ic_camera),
                ListBuilder.ICON_IMAGE,
                "Photo note"
            )
        )
    }

    companion object {
        const val TAG = "NoteSliceBuilder"
    }
}
