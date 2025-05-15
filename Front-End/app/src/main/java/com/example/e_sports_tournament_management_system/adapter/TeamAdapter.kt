package com.example.e_sports_tournament_management_system.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_sports_tournament_management_system.R
import com.example.e_sports_tournament_management_system.model.Team

class TeamAdapter(
    private val teamList: List<Team>,
    private val onEdit: (Team) -> Unit,
    private val onDelete: (Team) -> Unit
) : RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    class TeamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTeamName: TextView = itemView.findViewById(R.id.textTeamName)
        val textTeamCountry: TextView = itemView.findViewById(R.id.textTeamCountry)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team, parent, false)
        return TeamViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val team = teamList[position]
        holder.textTeamName.text = team.name
        holder.textTeamCountry.text = "Pays : ${team.country}"

        holder.btnEdit.setOnClickListener { onEdit(team) }
        holder.btnDelete.setOnClickListener { onDelete(team) }
    }

    override fun getItemCount(): Int = teamList.size
}
