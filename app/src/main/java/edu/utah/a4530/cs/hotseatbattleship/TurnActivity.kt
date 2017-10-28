package edu.utah.a4530.cs.hotseatbattleship

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_turn_switch.*

/**
 * Created by Nico on 10/23/2017.
 */
class TurnActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nextTurnButton.setOnClickListener {
            val lastPlayer = intent.getStringExtra("player")
            val i = Intent(this, PlayerActivity::class.java)
            when (lastPlayer) {
                "Player1" -> i.putExtra("player", "Player2")
                "Player2" -> i.putExtra("player", "Player1")
            }
            startActivity(i)
        }
    }

}