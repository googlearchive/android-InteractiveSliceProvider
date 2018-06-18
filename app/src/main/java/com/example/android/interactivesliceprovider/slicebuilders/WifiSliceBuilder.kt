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
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.SliceAction
import androidx.slice.builders.header
import androidx.slice.builders.list
import androidx.slice.builders.row
import androidx.slice.builders.seeMoreRow
import com.example.android.interactivesliceprovider.InteractiveSliceProvider
import com.example.android.interactivesliceprovider.MainActivity
import com.example.android.interactivesliceprovider.MyBroadcastReceiver
import com.example.android.interactivesliceprovider.R
import com.example.android.interactivesliceprovider.R.drawable
import com.example.android.interactivesliceprovider.SliceBuilder

class WifiSliceBuilder(
    val context: Context,
    val sliceUri: Uri
) : SliceBuilder {

    override fun buildSlice(): Slice {
        // Get wifi state
        val wifiManager = context
            .applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiState = wifiManager.wifiState
        var wifiEnabled = false
        val state: String
        when (wifiState) {
            WifiManager.WIFI_STATE_DISABLED, WifiManager.WIFI_STATE_DISABLING -> state =
                    "disconnected"
            WifiManager.WIFI_STATE_ENABLED, WifiManager.WIFI_STATE_ENABLING -> {
                state = wifiManager.connectionInfo.ssid
                wifiEnabled = true
            }
            WifiManager.WIFI_STATE_UNKNOWN -> state = "" // just don't show anything?
            else -> state = ""
        }

        // Set the first row as a toggle
        val finalWifiEnabled = wifiEnabled
        val primaryAction = SliceAction(
            MainActivity.getIntent(context, Settings.ACTION_WIFI_SETTINGS),
            IconCompat.createWithResource(context, drawable.ic_wifi), "Wi-fi Settings"
        )
        val toggleCDString = if (wifiEnabled) "Turn wifi off" else "Turn wifi on"
        val sliceCDString = if (wifiEnabled)
            "Wifi connected to $state"
        else
            "Wifi disconnected, 10 networks available"
        return list(context, sliceUri, ListBuilder.INFINITY) {
            setAccentColor(0xff4285)
            header {
                setTitle("Wi-fi")
                setSubtitle(state)
                setContentDescription(sliceCDString)
                setPrimaryAction(primaryAction)
            }
            addAction(
                SliceAction(
                    MyBroadcastReceiver.getIntent(
                        context, InteractiveSliceProvider.ACTION_WIFI_CHANGED, null
                    ),
                    toggleCDString, finalWifiEnabled
                )
            )

            // Add fake wifi networks
            val wifiIcons =
                intArrayOf(R.drawable.ic_wifi_full, R.drawable.ic_wifi_low, R.drawable.ic_wifi_fair)
            for (i in 0..9) {
                val iconId = wifiIcons[i % wifiIcons.size]
                val icon = IconCompat.createWithResource(context, iconId)
                val networkName = "Network$i"
                row {
                    setTitle(networkName)
                    setTitleItem(icon, ListBuilder.ICON_IMAGE)
                    val locked = i % 3 == 0
                    if (locked) {
                        addEndItem(
                            IconCompat.createWithResource(context, R.drawable.ic_lock),
                            ListBuilder.ICON_IMAGE
                        )
                        setContentDescription("Connect to $networkName, password needed")
                    } else {
                        setContentDescription("Connect to $networkName")
                    }
                    val message =
                        if (locked) "Open wifi password dialog" else "Connect to $networkName"
                    setPrimaryAction(
                        SliceAction(
                            MyBroadcastReceiver.getIntent(
                                context, InteractiveSliceProvider.ACTION_TOAST, message
                            ),
                            icon,
                            message
                        )
                    )
                }
            }

            // Add see more intent
            seeMoreRow {
                setTitle("See all available networks")
                addEndItem(
                    IconCompat.createWithResource(context, R.drawable.ic_right_caret),
                    ListBuilder.SMALL_IMAGE
                )
                setPrimaryAction(primaryAction)
            }
        }
    }

    companion object {
        const val TAG = "ListSliceBuilder"
    }
}