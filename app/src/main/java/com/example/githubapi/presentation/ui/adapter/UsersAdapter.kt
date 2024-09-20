package com.example.githubapi.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.githubapi.R
import com.example.githubapi.data.User
import com.example.githubapi.databinding.ItemUsersBinding


class UsersAdapter(
    private val onClickInterface: OnClickInterface
) : ListAdapter<User, UsersAdapter.ViewHolder>(UsersDiffUtil), Filterable {
    var userList = mutableListOf<User>()

    inner class ViewHolder(val binding: ItemUsersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            tvUserName.text = currentList[position].userName
            tvId.text = holder.itemView.context.getString(R.string.id) + currentList[position].id
            img.load(currentList[position].avatarUrl) {
                placeholder(R.mipmap.ic_launcher)
                error(R.mipmap.ic_launcher)
            }

            imgBtn.setOnClickListener {
                onClickInterface.onItemClick(currentList[position])
            }
        }
    }

    private val searchFilter: Filter = object : Filter() {
        override fun performFiltering(input: CharSequence): FilterResults {
            val filterResult = FilterResults()
            val filteredList = if (input.isEmpty()) {
                filterResult.apply {
                    values = userList
                    count = userList.size
                }
                userList
            } else {
                userList.filter { it.userName.lowercase().contains(input) }
            }
            return filterResult.apply {
                values = filteredList
                count = filteredList.size
            }
        }

        override fun publishResults(input: CharSequence, results: FilterResults) {
            submitList(results.values as ArrayList<User>)
        }
    }

    override fun getFilter(): Filter {
        return searchFilter
    }

    fun setData(list: MutableList<User>) {
        this.userList = list
        submitList(list)
    }

    interface OnClickInterface {
        fun onItemClick(user: User)
    }
}
