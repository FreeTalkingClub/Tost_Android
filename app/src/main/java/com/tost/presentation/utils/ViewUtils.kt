package com.tost.presentation.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.tost.R
import com.tost.databinding.WidgetTostToastBinding
import kotlinx.coroutines.*

/**
 * Created By Malibin
 * on 12월 03, 2020
 */

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(@StringRes stringResId: Int) {
    Toast.makeText(this, stringResId, Toast.LENGTH_SHORT).show()
}

fun Context.showTostToast(message: String) {
    val binding = WidgetTostToastBinding.inflate(LayoutInflater.from(this))
    binding.textMessage.text = message
    val topMargin = resources.getDimensionPixelOffset(R.dimen.tost_toast_top_margin)
    Toast(this).apply {
        view = binding.root
        duration = Toast.LENGTH_SHORT
        setGravity(Gravity.TOP, 0, topMargin)
    }.show()
}

fun View.disableTemporary(millis: Long) {
    this.isEnabled = false
    CoroutineScope(Dispatchers.IO).launch {
        delay(millis)
        withContext(Dispatchers.Main) { this@disableTemporary.isEnabled = true }
    }
}
