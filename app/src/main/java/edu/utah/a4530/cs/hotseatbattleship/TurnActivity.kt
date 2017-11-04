package edu.utah.a4530.cs.hotseatbattleship

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_turn_switch.*

/**
 * Created by Nico on 10/23/2017.
 */
class TurnActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turn_switch)
        // still need to pass on index
        val index = intent.getIntExtra("index", 0)
        val message = intent.getStringExtra("message")

        resultMessage.text = message

        nextTurnButton.setOnClickListener {
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