package edu.utah.a4530.cs.hotseatbattleship

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by Nico on 10/26/2017.
 */
class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val writePermissionCode: Int = 1
        private lateinit var playerInfo: PlayerInfo
        private lateinit var otherPlayerInfo: PlayerInfo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // load data for new player
        val player = intent.getStringExtra("player")
        val index = intent.getIntExtra("index", 0)

        // get playerinfo from chosen game object
        // we'll increment turn at the end
        // I think I need both boards and just display less information from one
        if (player == "Player 1") {
            playerInfo = GameCollection[index].p1Info
            otherPlayerInfo = GameCollection[index].p2Info
        }
        else {
            playerInfo = GameCollection[index].p2Info
            otherPlayerInfo = GameCollection[index].p1Info
        }
    }
}