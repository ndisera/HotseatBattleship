package edu.utah.a4530.cs.hotseatbattleship

import java.util.*

/**
 * Created by Nico on 10/23/2017.
 */
data class Ship(val name: String, val location: IntArray, val positionStates: IntArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Ship

        if (!Arrays.equals(location, other.location)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(location)
    }

    fun changePositionState(square: Int, state: String) {
        when (state) {
            "sunk" -> this.positionStates[square] = 3
            "hit" -> this.positionStates[square] = 2
            "miss" -> this.positionStates[square] = 1
            else -> this.positionStates[square] = 0
        }
    }
}