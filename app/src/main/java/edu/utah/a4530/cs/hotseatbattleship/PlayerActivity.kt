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
        private lateinit var playerInfo: PlayerInfo
        private lateinit var otherPlayerInfo: PlayerInfo
        private lateinit var message: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_turn)

        // load data for new player
        val player = intent.getStringExtra("player")
        val index = intent.getIntExtra("index", 0)
        if (player == "P1") {
            playerInfo = GameCollection[index].p1Info
            otherPlayerInfo = GameCollection[index].p2Info
            playerTextView.text = "Player 1"
            topBoard.modelBoard = GameCollection[index].p2Info.board
            topBoard.ships = GameCollection[index].p2Info.board.shipArray
            bottomBoard.modelBoard = GameCollection[index].p1Info.board
            bottomBoard.ships = GameCollection[index].p1Info.board.shipArray

        } else {
            playerInfo = GameCollection[index].p2Info
            otherPlayerInfo = GameCollection[index].p1Info
            playerTextView.text = "Player 2"
            bottomBoard.modelBoard = GameCollection[index].p2Info.board
            bottomBoard.ships = GameCollection[index].p2Info.board.shipArray
            topBoard.ships = GameCollection[index].p1Info.board.shipArray
            topBoard.modelBoard = GameCollection[index].p1Info.board
        }
        bottomBoard.displayShips = true

        topBoard.setOnRectChosenListener { boardView, rect ->
            // check to make sure spot isn't marked and game isn't complete
            if ((boardView.modelBoard.board[rect] != 0 && boardView.modelBoard.board[rect] != 1) || GameCollection[index].turn == "Game Over") {
                return@setOnRectChosenListener
            }

            // Miss
            val i = Intent(this, TurnActivity::class.java)
            i.putExtra("index", index)
            i.putExtra("player", player)
            // also need to pass in message to display in the turn view

            if (boardView.modelBoard.board[rect] == 0) {
                boardView.modelBoard.board[rect] = 2
                message = "You're attack was a miss!"
            }
            // Hit
            else if (boardView.modelBoard.board[rect] == 1) {
                boardView.modelBoard.board[rect] = 3
                message = "Your attack was a hit!"
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
                    if (checkShipSunkAndGameWon(ship, GameCollection[index].p1Info)) {
                        GameCollection[index].turn = "Game Over"
                        GameCollection[index].state = "Player 1 Won"
                    }
                }
            }
            for (ship in GameCollection[index].p2Info.board.shipArray) {
                if (!ship.sunk) {
                    if (checkShipSunkAndGameWon(ship, GameCollection[index].p2Info)) {
                        GameCollection[index].turn = "Game Over"
                        GameCollection[index].state = "Player 2 Won"
                    }
                }
            }

            GameCollection.saveDataset()
            i.putExtra("message", message)
            startActivity(i)
            finish()
        }

        exitGameButton.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    // checks if a ship was sunk, do this for the 5 ships on each board
    private fun checkShipSunkAndGameWon(ship: Ship, pInfo: PlayerInfo): Boolean {
        var sunk = true
        for (coord in ship.location) {
            if (pInfo.board.board[coord] != 3) {
                sunk = false
                break
            }
        }
        ship.sunk = sunk
        if (ship.sunk) {
            message = "Your attack was a hit! You've sunk your opponent's ${ship.name}!"
            for (coord in ship.location) {
                pInfo.board.board[coord] = 4
            }
            pInfo.shipsLeft--
            if (pInfo.shipsLeft == 0) {
                // game has been won
                message = "You sunk your opponent's last ship, their ${ship.name}! You've won the game!"
                // at this point I need to go to a different page or make the screen non-editable
                return true
            }
        }
        return false
    }
}