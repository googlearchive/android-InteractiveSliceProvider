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
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.slice.SliceManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        grantSlicePermissions(
            Uri.parse("content://com.example.android.interactivesliceprovider/hello")
        )
        setContentView(R.layout.activity_main)
    }

    private fun grantSlicePermissions(uri: Uri) {
        // Grant permissions to AGSA
        SliceManager.getInstance(this).grantSlicePermission(
            "com.google.android.googlequicksearchbox",
            uri
        )
        // grant permission to GmsCore
        SliceManager.getInstance(this).grantSlicePermission(
            "com.google.android.gms",
            uri
        )
        // Notify change. Ensure that it does not happen on every onCreate()
        // calls as notify change triggers reindexing which can clear usage
        // signals of your app and hence impact your app’s ranking. One way to
        // do this is to use shared preferences.
        val sharedPreferences = getSharedPreferences(
            "SlicePermissionGranted", Context.MODE_PRIVATE
        )
        if (!sharedPreferences.getBoolean("PermissionStatus", false)) {
            contentResolver.notifyChange(uri, null /* content observer */)
            sharedPreferences.edit {
                putBoolean("PermissionStatus", true)
            }
        }
    }

    companion object {
        fun getPendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, MainActivity::class.java)
            return PendingIntent.getActivity(context, 0, intent, 0)
        }
    }
}