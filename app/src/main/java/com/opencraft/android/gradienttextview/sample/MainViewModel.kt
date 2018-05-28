package com.opencraft.android.gradienttextview.sample

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField

class MainViewModel : ViewModel() {
    val text = ObservableField<String>("aaaaaaaaaa\naaaaaaaaaa\naaaaaaaaaa\naaaaaaaaaa\naaaaaaaaaa")
}