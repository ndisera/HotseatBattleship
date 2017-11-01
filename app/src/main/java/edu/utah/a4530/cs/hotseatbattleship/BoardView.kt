package edu.utah.a4530.cs.hotseatbattleship

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by Nico on 10/31/2017.
 */
class BoardView : View {
    constructor(context: Context?) : super(context) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes){
        init()
    }

    private fun init() {
        paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.color = Color.LTGRAY
        paint.strokeWidth = 2f
        paint.strokeCap = Paint.Cap.ROUND
    }

    // have an array of rects
    // for each rect in this array, draw it
    // should be relatively easy to setup in init or onSizeChanged
    val rectGrid : Array<RectF?> = arrayOfNulls(100)
    lateinit var paint: Paint

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas !is Canvas) return

        for (rect in rectGrid) {
            canvas.drawRect(rect, paint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val totalWidth = width - paddingLeft - paddingRight
        val totalHeight = height - paddingTop - paddingBottom

        val gridLength = Math.min(totalWidth, totalHeight)
        // depending on which is larger, I'll want to start and end at the (larger one - the smaller one) / 2

        val cellLength = gridLength / 10f

        var initialLeft: Float = 0f
        var initialTop: Float = 0f

        if (gridLength == totalWidth) {
            // width is smaller, need to adjust height
            initialTop = (totalHeight - totalWidth) / 2f
        }
        else if (gridLength == totalHeight) {
            // height is smaller, need to adjust width
            initialLeft = (totalWidth - totalHeight) / 2f
        }

        var left: Float = initialLeft
        var top: Float = initialTop
        var right: Float = left + cellLength
        var bottom: Float = top + cellLength


        // creates grid to be drawn
        // will check grid from datacollection to obtain colors for each cell
        for (i in 0..99) {
            if (i % 10 == 0 && i != 0) {
                // change top and bottom
                top += cellLength
                bottom = top + cellLength
                left = initialLeft
                right = left + cellLength

            }
            rectGrid[i] = RectF(left, top, right, bottom)
            right += cellLength
            left += cellLength
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event !is MotionEvent)
            return super.onTouchEvent(event)
        // do stuff
        return super.onTouchEvent(event)
    }

}