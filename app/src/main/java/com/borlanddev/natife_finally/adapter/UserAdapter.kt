package com.borlanddev.natife_finally.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.borlanddev.natife_finally.databinding.ListUsersBinding
import com.borlanddev.natife_finally.model.User

class UserAdapter(private var onItemClick: (User) -> Unit) :
    RecyclerView.Adapter<UserHolder>() {

    private var users: List<User> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    fun updateUsersList(_users: List<User>) {
        users = _users
    }

    override fun getItemCount(): Int = users.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListUsersBinding.inflate(inflater, parent, false)
        return UserHolder(binding)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val user = users[position]
        holder.bind(user, onItemClick)
    }
}

class UserHolder(private val binding: ListUsersBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User, onItemClick: (User) -> Unit) {
        binding.userNameTextView.text = user.name
        binding.itemUser.setOnClickListener {
            onItemClick.invoke(user)
        }
    }
}

