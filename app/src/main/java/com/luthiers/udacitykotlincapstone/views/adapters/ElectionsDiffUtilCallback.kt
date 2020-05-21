package com.luthiers.udacitykotlincapstone.views.adapters

import androidx.recyclerview.widget.DiffUtil
import com.luthiers.udacitykotlincapstone.data.models.SingleElection

class ElectionsDiffUtilCallback: DiffUtil.ItemCallback<SingleElection>() {
    override fun areItemsTheSame(oldItem: SingleElection, newItem: SingleElection) =
        (oldItem.id == newItem.id)

    override fun areContentsTheSame(oldItem: SingleElection, newItem: SingleElection) =
        true // if the items are the same that means that the contents are the same
}