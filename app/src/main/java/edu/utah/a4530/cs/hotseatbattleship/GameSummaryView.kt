package edu.utah.a4530.cs.hotseatbattleship

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.view_game_summary.view.stateTextView as importedStateTextView
import kotlinx.android.synthetic.main.view_game_summary.view.turnTextView as importedTurnTextView
import kotlinx.android.synthetic.main.view_game_summary.view.p1UnsunkShipsTextView as importedP1UnsunkShipsTextView
import kotlinx.android.synthetic.main.view_game_summary.view.p2UnsunkShipsTextView as importedP2UnsunkShipsTextView

/**
 * Created by Nico on 10/26/2017.
 */
class GameSummaryView: FrameLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var stateTextView = TextView(context)
    private var turnTextView = TextView(context)
    private var p1UnsunkShipsTextView = TextView(context)
    private var p2UnsunkShipsTextView = TextView(context)

    var state: String
        get() = stateTextView.text.toString()
        set(newState) {
            stateTextView.text = newState
        }

    var turn: String
        get() = turnTextView.text.toString()
        set(newTurn) {
            turnTextView.text = newTurn
        }

    var p1UnsunkShips: String
        get() = p1UnsunkShipsTextView.text.toString()
        set(newP1Unsunk) {
            p1UnsunkShipsTextView.text = newP1Unsunk
        }

    var p2UnsunkShips: String
        get() = p2UnsunkShipsTextView.text.toString()
        set(newP2Unsunk) {
            p2UnsunkShipsTextView.text = newP2Unsunk
        }

    override fun onFinishInflate() {
        super.onFinishInflate()
        stateTextView = importedStateTextView
        turnTextView = importedTurnTextView
        p1UnsunkShipsTextView = importedP1UnsunkShipsTextView
        p2UnsunkShipsTextView = importedP2UnsunkShipsTextView
    }
}