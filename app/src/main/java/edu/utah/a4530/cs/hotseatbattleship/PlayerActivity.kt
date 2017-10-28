package edu.utah.a4530.cs.hotseatbattleship

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by Nico on 10/26/2017.
 */
class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // load data for new player
        val player = intent.getStringExtra("player")

        // get playerinfo from chosen game object
    }
}