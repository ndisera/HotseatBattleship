package edu.utah.a4530.cs.hotseatbattleship

import java.util.*

/**
 * Created by Nico on 10/23/2017.
 */
class Board {
    // true if space occupied by ship, false otherwise
    private val board = BooleanArray(100)
    private val carrier = Ship("carrier", IntArray(5), IntArray(5))
    private val battleship = Ship("battleship",  IntArray(4), IntArray(4))
    private val cruiser = Ship("cruiser",  IntArray(3), IntArray(3))
    private val submarine = Ship("submarine",  IntArray(3), IntArray(3))
    private val destroyer = Ship("destroyer",  IntArray(2), IntArray(2))

    init {
        // randomly assign positions of ships
        val random = Random()



        // if no assignment works, reassign start point and repeat

    }

    fun randomAssign(size: Int) {
        val random = Random()
        // randomly assign start point and then end point according to location size?
        // check that it doesn't go out of bounds and doesn't overlap any other ship
        var start = (0..99).random()
        // reassign if necessary
        while (board[start]) {
            start = (0..99).random()
        }

        var end: Int

        // 0 - 9
        // 0, 10, 20, ..., 90
        // cases
        when ((0..3).random()) {
            0 -> end = start - size
            1 -> end = start + size
            2 -> end = start - size * 10
            3 -> end = start + size * 10
        }

        // if end is negative, redo
        // if end is above 99, redo
        // if start starts at 79 and end ends at 82
        // if it overlaps with another ship (can just see if any of the positions are marked as true

        // just divide this up initially
    }

    // returns a random number provided in range
    fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) +  start
}