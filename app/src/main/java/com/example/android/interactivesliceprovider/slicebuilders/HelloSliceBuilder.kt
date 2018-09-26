package com.example.android.interactivesliceprovider.slicebuilders

import android.content.Context
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.SliceAction
import androidx.slice.builders.list
import androidx.slice.builders.row
import com.example.android.interactivesliceprovider.MainActivity
import com.example.android.interactivesliceprovider.R
import com.example.android.interactivesliceprovider.R.drawable
import com.example.android.interactivesliceprovider.SliceBuilder

class HelloSliceBuilder(
    val context: Context,
    sliceUri: Uri
) : SliceBuilder(sliceUri) {

    override fun buildSlice() = list(context, sliceUri, ListBuilder.INFINITY) {
        val action = SliceAction.create(
            MainActivity.getPendingIntent(context),
            IconCompat.createWithResource(context, drawable.slices_1),
            ListBuilder.LARGE_IMAGE,
            "Open app"
        )
        return list(context, sliceUri, ListBuilder.INFINITY) {
            setAccentColor(ContextCompat.getColor(context, R.color.slice_accent_color))
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