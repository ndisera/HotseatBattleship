package edu.utah.a4530.cs.hotseatbattleship

import java.util.*

/**
 * Created by Nico on 10/23/2017.
 */
class Board {
    // true if space occupied by ship, false otherwise
    val board = IntArray(100)
    private val carrier: Ship
    private val battleship: Ship
    private val cruiser: Ship
    private val submarine: Ship
    private val destroyer: Ship
    val shipArray: Array<Ship>
    val hitList: MutableList<Int>

    init {
        // randomly assign positions of ships
        carrier = Ship("Carrier", randomAssign(5), false)
        battleship = Ship("Battleship", randomAssign(4), false)
        cruiser = Ship("Cruiser", randomAssign(3), false)
        submarine = Ship("Submarine", randomAssign(3), false)
        destroyer = Ship("Destroyer", randomAssign(2), false)
        shipArray = arrayOf(carrier, battleship, cruiser, submarine, destroyer)
        hitList = mutableListOf()
    }

    // ----------------------AI Methods Begin-----------------------

    fun randomShot(): Int {
        var start = (0..99).random()
        while (board[start] != 0 && board[start] != 1) {
            start = (0..99).random()
        }
        return start
    }

    // guess up, down, left, or right (if valid)
    // keep in mind it's still possible to sink a ship from this
    fun fireAround(point: Int): Int {
        val nextPossibleHit: MutableList<Int> = mutableListOf()
        // not too high
        if (point >= 10 && (board[point - 10] == 0 || board[point - 10] == 1)) {
            nextPossibleHit.add(point - 10)
        }
        // not too low
        if (point < 90 && (board[point + 10] == 0 || board[point + 10] == 1)) {
            nextPossibleHit.add(point + 10)
        }
        // not too far left
        if (point % 10 != 0 && (board[point - 1] == 0 || board[point - 1] == 1)) {
            nextPossibleHit.add(point - 1)
        }
        // not too far right
        if (point % 10 != 9 && (board[point + 1] == 0 || board[point + 1] == 1)) {
            nextPossibleHit.add(point + 1)
        }

        // return -1 if no surrounding points are valid
        if (nextPossibleHit.isEmpty()) {
            return -1
        }

        if (nextPossibleHit.size < 2) {
            return nextPossibleHit[0]
        }
        return nextPossibleHit[(0 until nextPossibleHit.size).random()]
    }

    fun followingHit(): Int {
        val largest = hitList.max() ?: 0

        for (point in hitList) {
            if (point % 10 != 9 && point + 1 == largest && largest % 10 != 9 && (board[largest + 1] == 0 || board[largest + 1] == 1)) {
                return largest + 1
            } else if (point < 90 && point + 10 == largest && largest < 90 && (board[largest + 10] == 0 ||board[largest + 10] == 1)) {
                return largest + 10
            }
        }

        val smallest = hitList.min() ?: 0

        for (point in hitList) {
            if (point % 10 != 0 && point - 1 == smallest && smallest % 10 != 0 && (board[smallest - 1] == 0 || board[smallest - 1] == 1)) {
                return smallest - 1
            } else if (point >= 10 && point - 10 == smallest && smallest >= 10 && (board[smallest - 10] == 0 || board[smallest - 10] == 1)) {
                return smallest - 10
            }
        }

        // otherwise we're dealing with multiple ships
        var nextGuess = fireAround(largest)
        if (nextGuess != -1) {
            return nextGuess
        }
        nextGuess = fireAround(smallest)
        return nextGuess
    }

    fun cleanseHitList() {
        hitList.filter { board[it] != 3 }.forEach { hitList.remove(it) }
    }

    // --------------------AI Methods End-----------------------

    private fun randomAssign(size: Int): IntArray {
        val options = mutableListOf<IntArray>()

        // do this until we get a valid ship placement
        while (options.isEmpty()) {
            var start = (0..99).random()
            // reassign if necessary
            while (board[start] == 1) {
                start = (0..99).random()
            }

            // not too far left
            if (start % 10 >= size - 1) {
                val placement = IntArray(size)
                var available = true
                for (i: Int in 0 until size) {
                    placement[i] = start - i
                    if (board[start - i] == 1) {
                        available = false
                        break
                    }
                }
                if (available) {
                    options.add(placement)
                }
            }
            // not too far right
            if (10 - start % 10 >= size) {
                val placement = IntArray(size)
                var available = true
                for (i: Int in 0 until size) {
                    placement[i] = start + i
                    if (board[start + i] == 1) {
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
                    if (board[start - i * 10] == 1) {
                        available = false
                        break
                    }
                }
                if (available) {
                    options.add(placement)
                }
            }
            // not too low
            if (10 - start / 10 >= size) {
                val placement = IntArray(size)
                var available = true
                for (i: Int in 0 until size) {
                    placement[i] = start + i * 10
                    if (board[start + i * 10] == 1) {
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
        val coords = options[(0..options.size).random()]
        // set board cell value
        for (point in coords) {
            // set taken points to 1 to represent that that space is part of a ship
            board[point] = 1
        }
        return coords
    }

    // returns a random number provided in range
    private fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) + start
}