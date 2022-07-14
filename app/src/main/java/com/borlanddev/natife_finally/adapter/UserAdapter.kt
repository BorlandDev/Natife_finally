package com.borlanddev.natife_finally.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.borlanddev.natife_finally.databinding.ListUsersBinding
import com.borlanddev.natife_finally.model.User

class UserAdapter(private var onItemClick: (User) -> Unit) :
    ListAdapter<User, UserAdapter.UserHolder>(UsersDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder.create(parent)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }


    class UserHolder(private val binding: ListUsersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, onItemClick: (User) -> Unit) {
            binding.userNameTextView.text = user.name
            binding.itemUser.setOnClickListener {
                onItemClick.invoke(user)
            }
        }

        companion object {
            fun create(parent: ViewGroup): UserHolder {
                return UserHolder(
                    ListUsersBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    class UsersDiffCallback
        : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}

