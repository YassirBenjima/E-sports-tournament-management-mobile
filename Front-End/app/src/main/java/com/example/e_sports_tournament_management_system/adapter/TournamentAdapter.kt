package com.example.e_sports_tournament_management_system.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_sports_tournament_management_system.R
import com.example.e_sports_tournament_management_system.model.Tournament

class TournamentAdapter(
    private val tournaments: List<Tournament>,
    private val onClick: (Tournament) -> Unit
) : RecyclerView.Adapter<TournamentAdapter.TournamentViewHolder>() {

    inner class TournamentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textName: TextView = view.findViewById(R.id.textTournamentName)
        val textDates: TextView = view.findViewById(R.id.textTournamentDates)
        val textLocation: TextView = view.findViewById(R.id.textTournamentLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tournament, parent, false)
        return TournamentViewHolder(view)
    }

    override fun onBindViewHolder(holder: TournamentViewHolder, position: Int) {
        val tournament = tournaments[position]
        holder.textName.text = tournament.name
        holder.textDates.text = "${tournament.startDate} - ${tournament.endDate}"
        holder.textLocation.text = tournament.location
        holder.itemView.setOnClickListener { onClick(tournament) }
    }

    override fun getItemCount(): Int = tournaments.size
}
