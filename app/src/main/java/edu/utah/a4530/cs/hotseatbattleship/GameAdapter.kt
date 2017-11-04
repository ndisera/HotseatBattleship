package edu.utah.a4530.cs.hotseatbattleship

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Adapter for the RecyclerView.
 * Created by Nico on 10/26/2017.
 */
class GameAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnGameSelectedListener {
        fun gameSelected(game: Game)
    }

    var selectedItemIndex: Int = RecyclerView.NO_POSITION

    class GameViewHolder(val gameSummaryView: GameSummaryView) : RecyclerView.ViewHolder(gameSummaryView)

    private var onGameSelectedListener: OnGameSelectedListener? = null

    fun setOnGameSelectedListener(onGameSelectedListener: ((game: Game) -> Unit)) {
        this.onGameSelectedListener = object : OnGameSelectedListener {
            override fun gameSelected(game: Game) {
                onGameSelectedListener(game)
            }
        }
    }

    override fun getItemCount(): Int = GameCollection.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)

        return GameViewHolder(layoutInflater.inflate(R.layout.view_game_summary, parent, false) as GameSummaryView).apply {
            gameSummaryView.setOnClickListener {
                selectedItemIndex = adapterPosition
                onGameSelectedListener?.gameSelected(GameCollection[adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val datasetItem: Game = GameCollection[position]

        if (holder !is GameViewHolder) {
            throw AssertionError("Invalid ViewHolder was supplied for binding.")
        }
        holder.gameSummaryView.state = "Status: ${datasetItem.state}"
        holder.gameSummaryView.turn = "Turn: ${datasetItem.turn}"
        if (datasetItem.turn == "You" || datasetItem.state == "You Won" || datasetItem.state == "AI Won") {
            holder.gameSummaryView.p1UnsunkShips = "You: ${datasetItem.p1Info.shipsLeft} ships left"
            holder.gameSummaryView.p2UnsunkShips = "AI: ${datasetItem.p2Info.shipsLeft} ships left"
        }
        else {
            holder.gameSummaryView.p1UnsunkShips = "P1: ${datasetItem.p1Info.shipsLeft} ships left"
            holder.gameSummaryView.p2UnsunkShips = "P2: ${datasetItem.p2Info.shipsLeft} ships left"
        }

    }

}