package com.example.edistynytandroid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

class CustomTemperatureView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // your helper variables etc. can be here

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var temperature : Int = 0

    fun changeTemperature(temp: Int) {
        temperature = temp

        if(temperature > 20) {
            paint.color = Color.RED
            textPaint.color = Color.WHITE
        } else if (temperature > 10) {
            paint.color = Color.rgb(255, 165, 0)
            textPaint.color = Color.WHITE
        } else if (temperature > -10) {
            paint.color = Color.YELLOW
            textPaint.color = Color.BLACK
        } else if (temperature > -20) {
            paint.color = Color.CYAN
            textPaint.color = Color.BLACK
        } else {
            paint.color = Color.BLUE
            textPaint.color = Color.WHITE
        }

        // android ei oletuksena piirrä custom viewiä uusiksi
        invalidate()
        requestLayout()
    }
    init
    {
        // this is constructor of your component
        // all initializations go here
        paint.color = Color.BLUE
        textPaint.color = Color.WHITE
        textPaint.textSize = 90f
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // you can do all the drawing through the canvas-object
        // parameters: x-coordinate, y-coordinate, size, color
        canvas.drawCircle(width.toFloat() / 2, width.toFloat() / 2, width.toFloat() / 2, paint)

        // parameters: content, x, y, color
        canvas.drawText("${temperature}°C", width.toFloat() / 2, width.toFloat() / 2 + 25, textPaint);
        // here you can do all the drawing
    }


    val size = 150

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Try for a width based on our minimum
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        var w: Int = View.resolveSizeAndState(minw, widthMeasureSpec, 1)

        // if no exact size given (either dp or match_parent)
        // use this one instead as default (wrap_content)
        if (w == 0)
        {
            w = size * 2
        }

        // Whatever the width ends up being, ask for a height that would let the view
        // get as big as it can
        // val minh: Int = View.MeasureSpec.getSize(w) + paddingBottom + paddingTop
        // in this case, we use the height the same as our width, since it's a circle
        val h: Int = View.resolveSizeAndState(
            View.MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )

        setMeasuredDimension(w, h)
    }

// note, if you use this version, in your onDraw-method, remember to use the
// canvas size when drawing the circle. for example:
// canvas.width.toFloat()
}