package edu.utah.a4530.cs.hotseatbattleship

import java.util.*

/**
 * Created by Nico on 10/23/2017.
 */
class Board {
    // true if space occupied by ship, false otherwise
    private val board = BooleanArray(100)
    private val carrier: Ship
    private val battleship: Ship
    private val cruiser: Ship
    private val submarine: Ship
    private val destroyer: Ship

    init {
        // randomly assign positions of ships
        carrier = Ship("carrier", randomAssign(5), IntArray(5))
        battleship = Ship("battleship", randomAssign(4), IntArray(4))
        cruiser = Ship("cruiser", randomAssign(3), IntArray(3))
        submarine = Ship("submarine", randomAssign(3), IntArray(3))
        destroyer = Ship("destroyer", randomAssign(2), IntArray(2))

    }

    private fun randomAssign(size: Int): IntArray {
        val options = mutableListOf<IntArray>()

        // do this until we get a valid ship placement
        while (options.isEmpty()) {
            var start = (0..99).random()
            // reassign if necessary
            while (board[start]) {
                start = (0..99).random()
            }

            // not too far left
            if (start % 10 >= size - 1) {
                val placement = IntArray(size)
                var available = true
                for (i: Int in 0 until size) {
                    placement[i] = start - i
                    if (board[start - i]) {
                        available = false
                        break
                    }
                }
                if (available) {
                    options.add(placement)
                }
            }
            // not too far right
            if (10 - start % 10 >= size - 1) {
                val placement = IntArray(size)
                var available = true
                for (i: Int in 0 until size) {
                    placement[i] = start + i
                    if (board[start + i]) {
                        available = false
                        break
                    }
                }
                if (available) {
                    options.add(placement)
                }
            }
            // not too high
            if (start / 10 >= size - 1) {
                val placement = IntArray(size)
                var available = true
                for (i: Int in 0 until size) {
                    placement[i] = start - i * 10
                    if (board[start - i * 10]) {
                        available = false
                        break
                    }
                }
                if (available) {
                    options.add(placement)
                }
            }
            // not too low
            if (100 - start / 10 >= size - 1) {
                val placement = IntArray(size)
                var available = true
                for (i: Int in 0 until size) {
                    placement[i] = start + i * 10
                    if (board[start + i * 10]) {
                        available = false
                        break
                    }
                }
                if (available) {
                    options.add(placement)
                }
            }
        }
        // now randomly select one of the options
        return options[(0..options.size).random()]
    }

    // returns a random number provided in range
    private fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) + start
}