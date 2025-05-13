package com.example.e_sports_tournament_management_system.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_sports_tournament_management_system.R
import com.example.e_sports_tournament_management_system.model.User

class UserAdapter(private val users: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textFullName: TextView = view.findViewById(R.id.textFullName)
        val textEmail: TextView = view.findViewById(R.id.textEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.textFullName.text = user.fullName
        holder.textEmail.text = user.email
    }

    override fun getItemCount() = users.size
}
