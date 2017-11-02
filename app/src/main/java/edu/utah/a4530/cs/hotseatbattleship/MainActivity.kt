package edu.utah.a4530.cs.hotseatbattleship

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), GameAdapter.OnGameSelectedListener {

    companion object {
        private const val writePermissionCode: Int = 1
        private var selectedIndex: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            externalStoragePermissionsChanged(false)

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // don't need to show explanation
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), writePermissionCode)
            }
        } else {
            externalStoragePermissionsChanged(true)
        }

        gameGrid.layoutManager = LinearLayoutManager(this)
        val gameAdapter = GameAdapter()
        gameAdapter.setOnGameSelectedListener { game ->
            gameSelected(game)
        }
        selectedIndex = gameAdapter.selectedItemIndex
        gameGrid.adapter = gameAdapter

        newGameButton.setOnClickListener {
            GameCollection.add(Game("Starting", "P1", PlayerInfo(5, Board()), PlayerInfo(5, Board())))
            deleteGameButton.isEnabled = true
            GameCollection.reloadDataset()
            gameGrid.adapter.notifyDataSetChanged()
        }

        deleteGameButton.setOnClickListener {
            if (deleteGameButton.text == "Delete an existing game") {
                deleteGameButton.text = "Choose game to delete"
            } else {
                deleteGameButton.text = "Delete an existing game"
            }
        }

        if (GameCollection.size != 0) {
            deleteGameButton.isEnabled = true
        }
    }

    override fun gameSelected(game: Game) {
        if (deleteGameButton.text == "Delete an existing game") {
            val i = Intent(this, PlayerActivity::class.java)

            // pass in index to grab game from game collection (not sure if this index will work)
            if (gameGrid.adapter !is GameAdapter) {
                return
            }
            selectedIndex = (gameGrid.adapter as GameAdapter).selectedItemIndex
            i.putExtra("index", selectedIndex)
            i.putExtra("player", GameCollection[selectedIndex].turn)
            startActivity(i)
        } else {
            // it's in delete mode so instead of starting a game, delete it
            GameCollection.delete(game)
            GameCollection.reloadDataset()
            deleteGameButton.text = "Delete an existing game"
            if (GameCollection.size == 0) {
                deleteGameButton.isEnabled = false
            }
            gameGrid.adapter.notifyDataSetChanged()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == writePermissionCode) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                externalStoragePermissionsChanged(true)
            } else {
                externalStoragePermissionsChanged(false)
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun externalStoragePermissionsChanged(writable: Boolean) {
        if (writable) {
            GameCollection.reloadDataset()
        }
    }
}
