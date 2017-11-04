package edu.utah.a4530.cs.hotseatbattleship

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_player_turn.*

/**
 * Activity that handles user actions on their turns and acts as a controller.
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

        // set up the display for the user
        if (player == "P1" || player == "You" || GameCollection[index].state == "You Won" || GameCollection[index].state == "AI Won") {
            playerInfo = GameCollection[index].p1Info
            otherPlayerInfo = GameCollection[index].p2Info
            when (player) {
                "P1" -> playerTextView.text = getString(R.string.player_1)
                else -> playerTextView.text = getString(R.string.you)
            }
            topBoard.modelBoard = GameCollection[index].p2Info.board
            topBoard.ships = GameCollection[index].p2Info.board.shipArray
            bottomBoard.modelBoard = GameCollection[index].p1Info.board
            bottomBoard.ships = GameCollection[index].p1Info.board.shipArray

        } else {
            playerInfo = GameCollection[index].p2Info
            otherPlayerInfo = GameCollection[index].p1Info
            playerTextView.text = getString(R.string.player_2)
            bottomBoard.modelBoard = GameCollection[index].p2Info.board
            bottomBoard.ships = GameCollection[index].p2Info.board.shipArray
            topBoard.ships = GameCollection[index].p1Info.board.shipArray
            topBoard.modelBoard = GameCollection[index].p1Info.board
        }
        bottomBoard.displayShips = true

        exitGameButton.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        topBoard.setOnRectChosenListener { boardView, rect ->
            // Makes board unresponsive if game is over
            if ((boardView.modelBoard.board[rect] != 0 && boardView.modelBoard.board[rect] != 1) || GameCollection[index].turn == "Game Over") {
                return@setOnRectChosenListener
            }

            // Handle AI games differently
            if (GameCollection[index].turn == "You") {
                p1AndAI(boardView, rect)
                return@setOnRectChosenListener
            }

            if (boardView.modelBoard.board[rect] == 0) {
                boardView.modelBoard.board[rect] = 2
                message = "You're attack was a miss!"
            }
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
            for (ship in GameCollection[index].p1Info.board.shipArray) {
                if (!ship.sunk) {
                    if (checkShipSunkAndGameWon(ship, GameCollection[index].p1Info)) {
                        GameCollection[index].turn = "Game Over"
                        GameCollection[index].state = "Player 2 Won"
                    }
                }
            }
            for (ship in GameCollection[index].p2Info.board.shipArray) {
                if (!ship.sunk) {
                    if (checkShipSunkAndGameWon(ship, GameCollection[index].p2Info)) {
                        GameCollection[index].turn = "Game Over"
                        GameCollection[index].state = "Player 1 Won"
                    }
                }
            }

            GameCollection.saveDataset()

            val i = Intent(this, TurnActivity::class.java)
            i.putExtra("index", index)
            i.putExtra("player", player)
            i.putExtra("message", message)
            startActivity(i)
            finish()
        }
    }

    /**
     * Handles AI Games.
     */
    private fun p1AndAI(boardView: BoardView, rect: Int) {
        val index = intent.getIntExtra("index", 0)

        // Miss
        if (boardView.modelBoard.board[rect] == 0) {
            boardView.modelBoard.board[rect] = 2
        }
        // Hit
        else if (boardView.modelBoard.board[rect] == 1) {
            boardView.modelBoard.board[rect] = 3
        }
        // change status of game if it has already started
        if (GameCollection[index].state == "Starting") {
            GameCollection[index].state = "In Progress"
        }

        for (ship in GameCollection[index].p2Info.board.shipArray) {
            if (!ship.sunk) {
                if (checkShipSunkAndGameWon(ship, GameCollection[index].p2Info)) {
                    GameCollection[index].turn = "Game Over"
                    GameCollection[index].state = "You Won"
                }
            }
        }
        GameCollection.saveDataset()
        topBoard.invalidate()
        // this handles player 1 (you), now we have to handle the AI
        turnAI(index)
        bottomBoard.invalidate()

        if (GameCollection[index].turn == "Game Over") {
            val player = intent.getStringExtra("player")
            val i = Intent(this, TurnActivity::class.java)
            i.putExtra("index", index)
            i.putExtra("player", player)
            i.putExtra("message", message)
            startActivity(i)
            finish()
        }
    }

    /**
     * AI will select where to fire.
     */
    private fun turnAI(index: Int) {
        // select random spot
        // if any spots have been a hit (was going to do last but could go hit miss hit)
        // check random spot above, right, below, left (with constraints)
        // if that's a hit, check one of the spots on the opposite sides of these
        // if these are misses, then you've hit two ships, so check above or below (or left or right)
        // yeah...
        val chosenPoint: Int = when {
            GameCollection[index].p1Info.board.hitList.isEmpty() -> // this one chooses random spot
                GameCollection[index].p1Info.board.randomShot()

        // this one chooses spot if there are any hits
            GameCollection[index].p1Info.board.hitList.size == 1 -> GameCollection[index].p1Info.board.fireAround(GameCollection[index].p1Info.board.hitList[0])
            else -> GameCollection[index].p1Info.board.followingHit()
        }

        // miss
        if (bottomBoard.modelBoard.board[chosenPoint] == 0) {
            bottomBoard.modelBoard.board[chosenPoint] = 2
        }
        // hit
        else {
            bottomBoard.modelBoard.board[chosenPoint] = 3
            bottomBoard.modelBoard.hitList.add(chosenPoint)
        }

        for (ship in GameCollection[index].p1Info.board.shipArray) {
            if (!ship.sunk) {
                if (checkShipSunkAndGameWon(ship, GameCollection[index].p1Info)) {
                    GameCollection[index].turn = "Game Over"
                    GameCollection[index].state = "AI Won"
                }
            }
        }

        // check if any hits turned into sinks
        GameCollection[index].p1Info.board.cleanseHitList()
        GameCollection.saveDataset()
    }

    /**
     * Checks if a ship was sunk.
     */
    private fun checkShipSunkAndGameWon(ship: Ship, pInfo: PlayerInfo): Boolean {
        val sunk = ship.location.none { pInfo.board.board[it] != 3 }
        ship.sunk = sunk
        if (ship.sunk) {
            message = "Your attack was a hit! You've sunk your opponent's ${ship.name}!"
            for (coord in ship.location) {
                pInfo.board.board[coord] = 4
            }
            pInfo.shipsLeft--
            if (pInfo.shipsLeft == 0) {
                val index = intent.getIntExtra("index", 0)
                // game has been won
                message = "You sunk your opponent's last ship, their ${ship.name}! You've won the game!"
                if (GameCollection[index].turn == "You") {
                    if (pInfo == GameCollection[index].p1Info) {
                        message = "The AI sunk your last ship, your ${ship.name}! You've lost the game!"
                    }
                }
                return true
            }
        }
        return false
    }
}