package edu.utah.a4530.cs.hotseatbattleship

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_turn_switch.*

/**
 * Activity that handles the turn transition, as well as the screen displayed when a game is finished.
 * Created by Nico on 10/23/2017.
 */
class TurnActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turn_switch)
        // still need to pass on index
        val index = intent.getIntExtra("index", 0)
        val message = intent.getStringExtra("message")

        // It shouldn't be passed to someone since the game is over.
        if (GameCollection[index].turn == "Game Over") {
            passText.text = ""
        }
        resultMessage.text = message

        nextTurnButton.setOnClickListener {
            if (GameCollection[index].turn == "Game Over") {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
                return@setOnClickListener
            }

            // If game isn't finished, prepare info for other player
            val lastPlayer = intent.getStringExtra("player")
            val i = Intent(this, PlayerActivity::class.java)
            when (lastPlayer) {
                "P1" -> i.putExtra("player", "P2")
                else -> i.putExtra("player", "P1")
            }
            i.putExtra("index", index)
            startActivity(i)
            finish()
        }
    }

}