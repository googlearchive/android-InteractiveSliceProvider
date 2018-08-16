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
import com.example.android.interactivesliceprovider.MainActivity
import com.example.android.interactivesliceprovider.MyBroadcastReceiver
import com.example.android.interactivesliceprovider.R.drawable
import com.example.android.interactivesliceprovider.SliceBuilder
import com.example.android.interactivesliceprovider.data.model.AppIndexingMetadata

class HelloSliceBuilder(
    val context: Context,
    sliceUri: Uri,
    appIndexingMetaData: AppIndexingMetadata
) : SliceBuilder(sliceUri, appIndexingMetaData) {

    override fun buildSlice() = list(context, sliceUri, ListBuilder.INFINITY) {
        val action = SliceAction.create(
            MainActivity.getPendingIntent(context),
            IconCompat.createWithResource(context, drawable.slices_1),
            ListBuilder.LARGE_IMAGE,
            "Open app"
        )
        return list(context, sliceUri, ListBuilder.INFINITY) {
            setAccentColor(0xff4285)
            row {
                title = "Hello, this is a slice!"
                primaryAction = action
            }
        }
    }

    companion object {
        const val TAG = "HelloSliceBuilder"
    }
}