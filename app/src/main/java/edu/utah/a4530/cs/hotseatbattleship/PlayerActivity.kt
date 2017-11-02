package edu.utah.a4530.cs.hotseatbattleship

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_player_turn.*

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
        setContentView(R.layout.activity_player_turn)

        // load data for new player
        val player = intent.getStringExtra("player")
        val index = intent.getIntExtra("index", 0)

        // get playerinfo from chosen game object
        // we'll increment turn at the end
        // I think I need both boards and just display less information from one
        if (player == "P1") {
            playerInfo = GameCollection[index].p1Info
            otherPlayerInfo = GameCollection[index].p2Info
            playerTextView.text = "Player 1"
            topBoard.modelBoard = GameCollection[index].p2Info.board
            bottomBoard.modelBoard = GameCollection[index].p1Info.board
        } else {
            playerInfo = GameCollection[index].p2Info
            otherPlayerInfo = GameCollection[index].p1Info
            playerTextView.text = "Player 2"
            bottomBoard.modelBoard = GameCollection[index].p2Info.board
            topBoard.modelBoard = GameCollection[index].p1Info.board
        }
        bottomBoard.displayShips = true

        topBoard.setOnRectChosenListener { boardView, rect ->
            // Miss
            val i = Intent(this, TurnActivity::class.java)
            i.putExtra("index", index)
            i.putExtra("player", player)

            if (boardView.modelBoard.board[rect] == 0) {
                boardView.modelBoard.board[rect] = 2
            }
            // Hit
            else if (boardView.modelBoard.board[rect] == 1) {
                boardView.modelBoard.board[rect] = 3
                // need to check if this sinks the boat
                // I guess this will involve going through the boats and seeing if this sinks it
                // If it does then I need to update all of those points to a value of 4

            }
            // change status of game if it has already started
            if (GameCollection[index].state == "Starting") {
                GameCollection[index].state = "In Progress"
            }
            when (player) {
                "P1" -> GameCollection[index].turn = "P2"
                else -> GameCollection[index].turn = "P1"
            }

            // check to convert hits to sinks (have to do each ship)
            // this is pretty inefficient but gets the job done for now

            for (ship in GameCollection[index].p1Info.board.shipArray) {
                if (!ship.sunk) {
                    checkShipSunk(ship, GameCollection[index].p1Info)
                }
            }
            for (ship in GameCollection[index].p2Info.board.shipArray) {
                if (!ship.sunk) {
                    checkShipSunk(ship, GameCollection[index].p2Info)
                }
            }

            GameCollection.saveDataset()
            startActivity(i)
        }
        exitGameButton.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    // checks if a ship was sunk, do this for the 5 ships on each board
    private fun checkShipSunk(ship: Ship, pInfo: PlayerInfo) {
        var sunk = true
        for (coord in ship.location) {
            if (pInfo.board.board[coord] != 3) {
                sunk = false
                break
            }
        }
        ship.sunk = sunk
        if (ship.sunk) {
            for (coord in ship.location) {
                pInfo.board.board[coord] = 4
            }
            pInfo.shipsLeft--
            if (pInfo.shipsLeft == 0) {
                // game has been won
            }
        }
    }
}