package com.example.firebaserealtime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(var user : ArrayList<User>, var userClickInterface: UserClickInterface) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    class ViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        var tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        var tvPassword = view.findViewById<TextView>(R.id.tvPassword)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         var view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() : Int {
        return user.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvEmail.setText(user[position].email)
        holder.tvPassword.setText(user[position].password)


        holder.tvEmail.setOnClickListener {
            userClickInterface.userClick(user[position])
        }
        holder.tvPassword.setOnClickListener {
            userClickInterface.userClick(user[position])
        }

    }
}