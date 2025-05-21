package com.example.e_sports_tournament_management_system.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_sports_tournament_management_system.R
import com.example.e_sports_tournament_management_system.model.Match

class MatchAdapter(
    private val matchList: List<Match>,
    private val onEdit: (Match) -> Unit,
    private val onDelete: (Match) -> Unit
) : RecyclerView.Adapter<MatchAdapter.MatchViewHolder>() {

    class MatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMatchTeams: TextView = itemView.findViewById(R.id.textMatchTeams)
        val textMatchScore: TextView = itemView.findViewById(R.id.textMatchScore)
        val textMatchWinner: TextView = itemView.findViewById(R.id.textMatchWinner)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_match, parent, false)
        return MatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = matchList[position]
        holder.textMatchTeams.text = "${match.team1.name} vs ${match.team2.name}"
        holder.textMatchScore.text = "Score : ${match.result?.teamAScore ?: "-"} - ${match.result?.teamBScore ?: "-"}"
        holder.textMatchWinner.text = "Vainqueur : ${match.result?.winner?.name ?: "Indéfini"}"

        holder.btnEdit.setOnClickListener { onEdit(match) }
        holder.btnDelete.setOnClickListener { onDelete(match) }
    }

    override fun getItemCount(): Int = matchList.size
}
