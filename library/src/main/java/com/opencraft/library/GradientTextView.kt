package com.opencraft.library

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.databinding.InverseBindingMethod
import android.databinding.InverseBindingMethods
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Point
import android.graphics.Shader
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

@InverseBindingMethods(InverseBindingMethod(type = GradientTextView::class, attribute = "android:text"))
class GradientTextView constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : AppCompatTextView(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var gradientColors = intArrayOf(Color.BLACK, Color.WHITE)
    private var gradientColorPos = floatArrayOf(0f, 1f)
    private var linearGradient: LinearGradient? = null
    private var angle = 0.0
    private var startOption = -1
    private var endOption = -1

    val TOP_LEFT = 0
    val TOP_CENTER = 1
    val MIDDLE_RIGHT = 2
    val MIDDLE_LEFT = 3
    val TOP_RIGHT = 4
    val BOTTOM_LEFT = 5
    val BOTTOM_CENTER = 6
    val BOTTOM_RIGHT = 7

    init {
        attrs?.let {

            val typedArray = context.obtainStyledAttributes(it, R.styleable.GradientTextView, 0, 0)

            loadAngle(typedArray)

            loadColors(typedArray)

            loadColorsSpan(typedArray)

            loadPoints(typedArray)

            typedArray.recycle()
        }
    }

    private fun loadAngle(typedArray: TypedArray) {
        angle = typedArray.getInteger(R.styleable.GradientTextView_angle, 0).toDouble()
    }

    private fun loadPoints(typedArray: TypedArray) {
        startOption = typedArray.getInteger(R.styleable.GradientTextView_gradient_start, -1)
        endOption = typedArray.getInteger(R.styleable.GradientTextView_gradient_end, -1)
    }

    private fun loadColorsSpan(typedArray: TypedArray) {
        val colorsSpan = resources.getIntArray(typedArray
                .getResourceId(R.styleable.GradientTextView_color_span_array, R.array.gradient_color_span_default))
        colorsSpan?.let {
            gradientColorPos = FloatArray(it.size)
            gradientColorPos[0] = 0f //first color must be at the start
            val sum = it.sum().toFloat()
            for (i in 0..it.size - 2) {
                gradientColorPos[i + 1] = (1f / sum) * it[i]
            }
            gradientColorPos[it.size - 1] = 1f //last color must be at the end
        }
    }

    private fun loadColors(typedArray: TypedArray) {
        val colors = resources.getStringArray(typedArray
                .getResourceId(R.styleable.GradientTextView_color_array, R.array.gradient_default))
        colors?.let {
            gradientColors = IntArray(it.size)
            for (i in 0 until it.size) {
                gradientColors[i] = Color.parseColor(it[i])
            }
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {

            val startPoint = getStartPoint()
            val endPoint = getEndPoint()

            linearGradient = LinearGradient(startPoint.x.toFloat(), startPoint.y.toFloat(), endPoint.x.toFloat(), endPoint.y.toFloat(),
                    gradientColors,
                    gradientColorPos,
                    Shader.TileMode.CLAMP)
            paint.shader = linearGradient
        }
    }

    private fun getEndPoint(): Point {
        if (startOption > -1 && endOption > -1) {
            return getPointFromEnum(endOption)
        } else {
            return calcEndFromAngle()

        }
    }

    private fun calcEndFromAngle(): Point {
        val centerX = width / 2f
        val centerY = height / 2f
        val radians = Math.toRadians(angle)
        val endX = centerX + (0f - centerX) * Math.cos(radians) - (centerY - centerY) * Math.sin(radians);
        val endY = centerY + (0f - centerX) * Math.sin(radians) + (centerY - centerY) * Math.cos(radians);
        return Point(endX.toInt(), endY.toInt())
    }

    private fun getStartPoint(): Point {
        if (startOption > -1 && endOption > -1) {
            return getPointFromEnum(startOption)
        } else {
            return calcStartFromAngle()
        }
    }

    private fun getPointFromEnum(startOption: Int): Point {
        return when (startOption) {
            TOP_LEFT -> Point(0, 0)
            TOP_CENTER -> Point(width / 2, 0)
            MIDDLE_RIGHT -> Point(width, height / 2)
            MIDDLE_LEFT -> Point(0, height / 2)
            TOP_RIGHT -> Point(width, 0)
            BOTTOM_LEFT -> Point(0, height)
            BOTTOM_CENTER -> Point(width / 2, height)
            BOTTOM_RIGHT -> Point(width, height)
            else -> Point(0, 0)
        }
    }

    private fun calcStartFromAngle(): Point {
        val centerX = width / 2f
        val centerY = height / 2f

        val radians = Math.toRadians(angle)

        val startX = centerX + (width - centerX) * Math.cos(radians) - (centerY - centerY) * Math.sin(radians);
        val startY = centerY + (width - centerX) * Math.sin(radians) + (centerY - centerY) * Math.cos(radians);
        return Point(startX.toInt(), startY.toInt())
    }
}