package edu.utah.a4530.cs.hotseatbattleship

import java.util.*

/**
 * Represents a ship in the model.
 * Created by Nico on 10/23/2017.
 */
data class Ship(val name: String, val location: IntArray, var sunk: Boolean) {

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
}