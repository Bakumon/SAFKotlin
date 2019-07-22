package com.coolapk.safkotlin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.net.Uri

@SuppressLint("ApplySharedPref")
fun saveUri(context: Context, uri: Uri) {
    val sharedPreferences = context.getSharedPreferences("settings", MODE_PRIVATE)
    val edit = sharedPreferences.edit()
    edit.putString("uri", uri.toString())
    edit.commit()
}

fun getUri(context: Context): Uri? {
    val sharedPreferences = context.getSharedPreferences("settings", MODE_PRIVATE)
    return Uri.parse(sharedPreferences.getString("uri", ""))
}