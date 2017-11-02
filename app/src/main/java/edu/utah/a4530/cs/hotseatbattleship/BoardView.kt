package edu.utah.a4530.cs.hotseatbattleship

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import org.jetbrains.annotations.NotNull

/**
 * Created by Nico on 10/31/2017.
 */
class BoardView : View {
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.color = Color.LTGRAY
        paint.strokeWidth = 2f
        paint.strokeCap = Paint.Cap.ROUND

        modelBoard = Board()
    }

    // have an array of rects
    // for each rect in this array, draw it
    // should be relatively easy to setup in init or onSizeChanged
    val rectGrid: Array<RectF?> = arrayOfNulls(100)
    lateinit var paint: Paint
    // modelBoard
    // check if each cell is a hit, miss, null, or sunk
    lateinit var modelBoard: Board
    var displayShips = false
    private var initialLeft = 0f
    private var initialTop = 0f
    private var cellLength = 0f
    private var onRectChosenListener: OnRectChosenListener? = null

    interface OnRectChosenListener {
        fun onRectChosen(boardView: BoardView, rect: Int) {}
    }

    fun setOnRectChosenListener(onRectChosenListener: OnRectChosenListener) {
        this.onRectChosenListener = onRectChosenListener
    }

    fun setOnRectChosenListener(onRectChosenListener: ((boardView: BoardView, rect: Int) -> Unit)) {
        this.onRectChosenListener = object : OnRectChosenListener {
            override fun onRectChosen(boardView: BoardView, rect: Int) {
                onRectChosenListener(boardView, rect)
            }
        }
    }

    fun removeOnColorChangedListener() {
        onRectChosenListener = null
    }

    private fun invokeRectListener(rect: Int) {
        onRectChosenListener?.onRectChosen(this, rect)
        invalidate()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas !is Canvas) return

        for (i in 0..99) {
            // might want to think of a way to outline the edges of ships as a whole
            // probably have to drawRect around the whole thing after this for loop?
            // would need left, top, right, and bottom
            if (displayShips && (modelBoard.board[i] == 1 || modelBoard.board[i] == 3)) {
                paint.style = Paint.Style.FILL_AND_STROKE
            }
            canvas.drawRect(rectGrid[i], paint)

            if (modelBoard.board[i] != 0 && modelBoard.board[i] != 1) {
                paint.style = Paint.Style.FILL
                rectGrid[i]?.let {
                    paint.color = when (i) {
                        2 -> Color.WHITE
                        3 -> Color.RED
                        4 -> Color.RED
                        else -> paint.color
                    }
                    if (i == 4) {
                        canvas.drawRect(it.left, it.top, it.right, it.bottom, paint)

                    } else {
                        canvas.drawOval(it.left, it.top, it.right, it.bottom, paint)
                    }
                }
            }
            paint.color = Color.LTGRAY
            paint.style = Paint.Style.STROKE
        }

        // will have to check on what to display for each board
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val totalWidth = width - paddingLeft - paddingRight
        val totalHeight = height - paddingTop - paddingBottom

        val gridLength = Math.min(totalWidth, totalHeight)
        // depending on which is larger, I'll want to start and end at the (larger one - the smaller one) / 2

        cellLength = gridLength / 10f

        if (gridLength == totalWidth) {
            // width is smaller, need to adjust height
            initialTop = (totalHeight - totalWidth) / 2f
        } else if (gridLength == totalHeight) {
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

    // probably need to put this in PlayerActivity since this can trigger an intent
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event !is MotionEvent)
            return super.onTouchEvent(event)
        // do stuff
        // use event.x, event.y to detect where user clicked
        // could use int instead of bool, 0 if no ship, 1 if ship, 2 if miss, 3 if hit, 4 if sunk
        // convert x and y to cell

        // I think this portion is incorrect
        val locationX = (event.x - initialLeft) / cellLength
        val locationY = (event.y - initialTop) / cellLength
        val index = (locationY.toInt() * 10 + locationX.toInt())
        invokeRectListener(index)
        // check that index in board hasn't been attempted
        // make change to that index and go to turn screen

        return super.onTouchEvent(event)
    }

}