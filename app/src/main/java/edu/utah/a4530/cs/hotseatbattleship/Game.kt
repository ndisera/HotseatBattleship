package edu.utah.a4530.cs.hotseatbattleship

import com.google.gson.Gson
import org.json.JSONException

/**
 * Created by Nico on 10/23/2017.
 */
data class Game(var state: String, var turn: String, val p1Info: PlayerInfo, val p2Info: PlayerInfo) {
    companion object {

        fun fromJSON(JSON: String): Game? {
            val gson = Gson()
            return try {
                gson.fromJson(JSON, Game::class.java)
            } catch (exception: JSONException) {
                null
            }
        }
    }

    fun toJSON(): String {
        val gson = Gson()
        val drawing = Game(state, turn, p1Info, p2Info)
        return gson.toJson(drawing)
    }
}