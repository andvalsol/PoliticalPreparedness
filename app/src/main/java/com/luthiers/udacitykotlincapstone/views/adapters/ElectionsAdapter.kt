package com.luthiers.udacitykotlincapstone.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luthiers.udacitykotlincapstone.data.models.SingleElection
import com.luthiers.udacitykotlincapstone.databinding.SingleElectionBinding


interface IUpcomingElectionsAdapter {
    fun onSingleElectionClick(view: View, singleElection: SingleElection)
}

class ElectionsAdapter(private val iUpcomingElectionsAdapter: IUpcomingElectionsAdapter) :
    ListAdapter<SingleElection, ElectionsAdapter.UpcomingElectionsViewHolder>(ElectionsDiffUtilCallback()) {

    inner class UpcomingElectionsViewHolder(val binding: SingleElectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(singleElection: SingleElection) {
            with(binding) {
                this.singleElection = singleElection

                executePendingBindings()
            }

            binding.root.setOnClickListener {
                iUpcomingElectionsAdapter.onSingleElectionClick(
                    it, getItem(adapterPosition)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UpcomingElectionsViewHolder(
            SingleElectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: UpcomingElectionsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}