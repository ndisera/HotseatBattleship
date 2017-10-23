package edu.utah.a4530.cs.hotseatbattleship

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_turn_switch.*

/**
 * Created by Nico on 10/23/2017.
 */
class TurnActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nextTurnButton.setOnClickListener {
            val intent = Intent()
        }
    }

}