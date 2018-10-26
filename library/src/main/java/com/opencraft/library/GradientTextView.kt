package com.opencraft.library

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

class GradientTextView constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : AppCompatTextView(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var gradientColorPos = floatArrayOf(0f, 1f)
    private var angle = 0.0
    private var startOption = -1
    private var endOption = -1
    private var changedColors = true
    private var changedSize = false

    var gradientColors = intArrayOf(Color.BLACK, Color.WHITE)
        set(value) {
            field = value
            changedColors = true
            invalidate()
        }

    var gradientColorsSpan = intArrayOf(1, 1)
        set(value) {
            field = value
            changedColors = true
            invalidate()
        }

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

    private fun getLinearGradient(): LinearGradient {
        calcColorPos()
        val gradientStartPoint = getStartPoint()
        val gradientEndPoint = getEndPoint()
        return LinearGradient(gradientStartPoint.x.toFloat(),
                gradientStartPoint.y.toFloat(),
                gradientEndPoint.x.toFloat(),
                gradientEndPoint.y.toFloat(),
                gradientColors,
                gradientColorPos,
                Shader.TileMode.CLAMP)
    }

    private fun loadAngle(typedArray: TypedArray) {
        angle = typedArray.getInteger(R.styleable.GradientTextView_angle, 0).toDouble()
    }

    private fun loadPoints(typedArray: TypedArray) {
        startOption = typedArray.getInteger(R.styleable.GradientTextView_gradient_start, -1)
        endOption = typedArray.getInteger(R.styleable.GradientTextView_gradient_end, -1)
    }

    private fun loadColorsSpan(typedArray: TypedArray) {
        gradientColorsSpan = resources.getIntArray(typedArray
                .getResourceId(R.styleable.GradientTextView_color_span_array, R.array.gradient_color_span_default))
    }

    private fun calcColorPos() {
        gradientColorsSpan.let {
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
        gradientColors = resources.getIntArray(typedArray
                .getResourceId(R.styleable.GradientTextView_gradientColors, R.array.gradient_default))
    }

    override fun onDraw(canvas: Canvas?) {
        if (changedColors ) {
            changedColors = false
            paint.shader = getLinearGradient()
        }
        if (changedSize) {
            changedSize = false
            paint.shader = getLinearGradient()
        }
        super.onDraw(canvas)
    }

    private fun getEndPoint(): Point {
        return if (startOption > -1 && endOption > -1) {
            getPointFromEnum(endOption)
        } else {
            calcEndFromAngle()

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
        return if (startOption > -1 && endOption > -1) {
            getPointFromEnum(startOption)
        } else {
            calcStartFromAngle()
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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        changedColors = true
    }

    companion object {
        const val TOP_LEFT = 0
        const val TOP_CENTER = 1
        const val MIDDLE_RIGHT = 2
        const val MIDDLE_LEFT = 3
        const val TOP_RIGHT = 4
        const val BOTTOM_LEFT = 5
        const val BOTTOM_CENTER = 6
        const val BOTTOM_RIGHT = 7
    }
}