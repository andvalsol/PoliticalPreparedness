package com.luthiers.udacitykotlincapstone.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luthiers.udacitykotlincapstone.data.models.SingleRepresentative
import com.luthiers.udacitykotlincapstone.databinding.SingleRepresentativeBinding

class MyRepresentativesAdapter: RecyclerView.Adapter<MyRepresentativesAdapter.MyRepresentativesViewHolder>() {

    private val representatives = mutableListOf<SingleRepresentative>()

    internal fun addRepresentatives(representatives: List<SingleRepresentative>) {
        this.representatives.addAll(representatives)
    }

    inner class MyRepresentativesViewHolder(val binding: SingleRepresentativeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(singleRepresentative: SingleRepresentative) {
            with(binding) {
                this.singleRepresentative = singleRepresentative

                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyRepresentativesViewHolder(SingleRepresentativeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() =
        representatives.size

    override fun onBindViewHolder(holder: MyRepresentativesViewHolder, position: Int) {
        holder.bind(representatives[position])
    }
}