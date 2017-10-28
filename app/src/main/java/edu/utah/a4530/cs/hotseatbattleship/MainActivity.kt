package edu.utah.a4530.cs.hotseatbattleship

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    companion object {
        private const val writePermissionCode: Int = 1
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

        gameGrid.layoutManager = GridLayoutManager(this, 3)
        gameGrid.adapter = GameAdapter()

        newGameButton.setOnClickListener {

        }

        deleteGameButton.setOnClickListener {
            if (deleteGameButton.text == "Delete an existing game") {
                deleteGameButton.text = "Choose game to delete"
            }
            else {

            }
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
