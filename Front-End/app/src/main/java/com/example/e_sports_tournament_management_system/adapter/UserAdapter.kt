package com.example.e_sports_tournament_management_system.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_sports_tournament_management_system.R
import com.example.e_sports_tournament_management_system.model.User

class UserAdapter(
    private val userList: List<User>,
    private val onEdit: (User) -> Unit,
    private val onDelete: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textFullName: TextView = itemView.findViewById(R.id.textFullName)
        val textEmail: TextView = itemView.findViewById(R.id.textEmail)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.textFullName.text = user.fullName
        holder.textEmail.text = user.email

        holder.btnEdit.setOnClickListener { onEdit(user) }
        holder.btnDelete.setOnClickListener { onDelete(user) }
    }

    override fun getItemCount(): Int = userList.size
}

