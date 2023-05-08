package com.example.edistynytandroid

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView

class LatestDataView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var maxRows : Int = 5

    init {
        // vaihdetaan LinearLayoutin orientaatio pystysuuntaiseksi
        this.orientation = VERTICAL

        var someText: TextView = TextView(context)
        someText.measure(0,0)

        var rowHeight = someText.measuredHeight
        this.measure(0,0)

        var additionalHeight = this.measuredHeight + (maxRows * rowHeight)
        this.minimumHeight = additionalHeight

//        for (i in 0..maxRows) {
//            addData("")
//        }

    }


    fun addData(message : String)
    {
        while(this.childCount >= maxRows) {
            this.removeViewAt(0)
        }

        var newTextView : TextView = TextView(context) as TextView
        newTextView.setText(message)
        newTextView.setBackgroundColor(Color.BLACK)
        newTextView.setTextColor(Color.YELLOW)
        this.addView(newTextView)

        val animation = AnimationUtils.loadAnimation(context, R.anim.customfade)
        newTextView.startAnimation(animation)
    }
}