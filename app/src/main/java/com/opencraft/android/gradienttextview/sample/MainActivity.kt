package com.opencraft.android.gradienttextview.sample

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.opencraft.android.gradienttextview.R
import com.opencraft.android.gradienttextview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm = MainViewModel(this)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.vm = vm
//        binding.gradient.postDelayed({
//            binding.gradient.gradientColors = intArrayOf(Color.BLUE, Color.GREEN)
//        }, 1000)
    }
}
