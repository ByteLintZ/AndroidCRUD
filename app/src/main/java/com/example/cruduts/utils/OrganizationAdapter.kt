package com.example.cruduts.utils

import com.example.cruduts.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cruduts.model.Role

class OrganizationAdapter(private val roles: List<Role>) :
    RecyclerView.Adapter<OrganizationAdapter.RoleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_organization_card, parent, false)
        return RoleViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        val role = roles[position]
        holder.bind(role)
    }

    override fun getItemCount(): Int = roles.size

    class RoleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgRoleIcon: ImageView = itemView.findViewById(R.id.imgRoleIcon)
        private val tvRoleTitle: TextView = itemView.findViewById(R.id.tvRoleTitle)
        private val tvRoleDescription: TextView = itemView.findViewById(R.id.tvRoleDescription)

        fun bind(role: Role) {
            imgRoleIcon.setImageResource(role.iconResId)
            tvRoleTitle.text = role.title
            tvRoleDescription.text = role.description
        }
    }
}
