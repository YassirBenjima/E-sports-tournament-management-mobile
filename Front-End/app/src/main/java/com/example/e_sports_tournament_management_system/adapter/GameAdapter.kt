package com.example.e_sports_tournament_management_system.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_sports_tournament_management_system.R
import com.example.e_sports_tournament_management_system.model.Game

class GameAdapter(
    private val gameList: List<Game>,
    private val onEdit: (Game) -> Unit,
    private val onDelete: (Game) -> Unit
) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textGameName: TextView = itemView.findViewById(R.id.textGameName)
        val textPlatform: TextView = itemView.findViewById(R.id.textPlatform)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gameList[position]
        holder.textGameName.text = game.name
        holder.textPlatform.text = game.platform

        holder.btnEdit.setOnClickListener { onEdit(game) }
        holder.btnDelete.setOnClickListener { onDelete(game) }
    }

    override fun getItemCount(): Int = gameList.size
}

