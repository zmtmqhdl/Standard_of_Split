@file:Suppress("DEPRECATION")

package com.example.standardofsplit.presentation.ui.component

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.standardofsplit.R

fun showCustomToast(
    context: Context,
    message: String
) {
    val toast = Toast(context)
    toast.duration = Toast.LENGTH_SHORT
    val text = TextView(context).apply {
        this.text = message
        setTextColor(Color.WHITE)
        textSize = 16f
        setPadding(25, 16, 25, 16)
        background = ContextCompat.getDrawable(context, R.drawable.toast_background)
    }
    toast.view = text
    toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 150)
    toast.show()
}
