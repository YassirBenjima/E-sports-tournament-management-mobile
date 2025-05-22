package com.example.e_sports_tournament_management_system.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_sports_tournament_management_system.R
import com.example.e_sports_tournament_management_system.model.Tournament

class TournamentAdapter(
    private val tournaments: List<Tournament>,
    private val onDetails: (Tournament) -> Unit,
    private val onEdit: (Tournament) -> Unit,
    private val onDelete: (Tournament) -> Unit
) : RecyclerView.Adapter<TournamentAdapter.TournamentViewHolder>() {

    inner class TournamentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textName: TextView = view.findViewById(R.id.textTournamentName)
        val textDates: TextView = view.findViewById(R.id.textTournamentDates)
        val textLocation: TextView = view.findViewById(R.id.textTournamentLocation)
        val btnDetails: ImageButton = view.findViewById(R.id.btnDetails)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
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

        holder.btnDetails.setOnClickListener { onDetails(tournament) }
        holder.btnEdit.setOnClickListener { onEdit(tournament) }
        holder.btnDelete.setOnClickListener { onDelete(tournament) }
    }

    override fun getItemCount(): Int = tournaments.size
}

