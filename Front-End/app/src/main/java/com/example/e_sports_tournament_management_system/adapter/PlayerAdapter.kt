package com.example.e_sports_tournament_management_system.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_sports_tournament_management_system.R
import com.example.e_sports_tournament_management_system.model.Player

class PlayerAdapter(
    private val playerList: List<Player>,
    private val onEdit: (Player) -> Unit,
    private val onDelete: (Player) -> Unit
) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textUsername: TextView = itemView.findViewById(R.id.textPlayerUsername)
        val textNationality: TextView = itemView.findViewById(R.id.textPlayerNationality)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false)
        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = playerList[position]
        holder.textUsername.text = player.username
        holder.textNationality.text = "Âge : ${player.age} | Nationalité : ${player.nationality}"

        holder.btnEdit.setOnClickListener { onEdit(player) }
        holder.btnDelete.setOnClickListener { onDelete(player) }
    }

    override fun getItemCount(): Int = playerList.size
}
