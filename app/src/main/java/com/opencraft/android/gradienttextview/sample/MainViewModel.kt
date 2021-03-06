package com.opencraft.android.gradienttextview.sample

import android.content.Context
import android.databinding.ObservableField
import com.opencraft.android.gradienttextview.R

class MainViewModel(context: Context) {
    val text = ObservableField<String>()
    val colors = context.resources.getIntArray(R.array.gradient_2)
    val colorsSpan = context.resources.getIntArray(R.array.gradient_color_span_2)
}