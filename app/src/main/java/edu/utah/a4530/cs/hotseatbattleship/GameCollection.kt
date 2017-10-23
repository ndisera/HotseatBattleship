package edu.utah.a4530.cs.hotseatbattleship

import android.os.Environment
import java.io.File

/**
 * Created by Nico on 10/23/2017.
 */
object GameCollection {
    private const val datasetDirectoryName: String = "GameCollection"
    private const val drawingExtension: String = ".battleshipgame"

    val size: Int
        get() = dataset.size

    private var dataset: MutableList<Game> = mutableListOf()

    private val canReadExternalStorage: Boolean
        get() = when (Environment.getExternalStorageState()) {
            Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY -> true
            else -> false
        }

    private val canWriteToExternalStorage: Boolean
        get() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

    operator fun get(index: Int): Game {
        return dataset[index]
    }

    operator fun set(index: Int, Game: Game) {
        dataset[index] = Game
        saveDataset()
    }

    fun add(Game: Game) {
        dataset.add(Game)
        saveDataset()
    }

    fun delete(Game: Game) {
        dataset.remove(Game)
        saveDataset()
    }

    fun reloadDataset() {
        val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), datasetDirectoryName)

        if (canReadExternalStorage && directory.exists()) {
            dataset = directory.listFiles().filter { it.absolutePath.endsWith(drawingExtension) }.mapNotNull {
                Game.fromJSON(String.fromFile(it.absolutePath) ?: "")
            }.toMutableList()
        }
    }

    fun saveDataset() {
        if (canWriteToExternalStorage) {
            val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), datasetDirectoryName)
            directory.recursiveDelete()

            if (!directory.exists()) {
                check(directory.mkdirs(), {
                    "External storage was marked as readable, but a directory could not be created there"
                })
            }
            var index = 0
            dataset.forEach {
                it.toJSON().toFile(directory.absolutePath + File.separator + index++ + drawingExtension)
            }
        }
    }

    private fun File.recursiveDelete() {
        if (this.exists()) {
            if (this.isDirectory) {
                this.listFiles().forEach {
                    it.recursiveDelete()
                }
            } else {
                this.delete()
            }
        }
    }
}