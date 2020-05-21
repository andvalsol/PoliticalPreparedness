package com.luthiers.udacitykotlincapstone.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.luthiers.udacitykotlincapstone.R
import com.luthiers.udacitykotlincapstone.data.models.SingleRepresentative
import com.luthiers.udacitykotlincapstone.databinding.SingleRepresentativeBinding

class MyRepresentativesAdapter: RecyclerView.Adapter<MyRepresentativesAdapter.MyRepresentativesViewHolder>() {

    companion object {
        @JvmStatic @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String?) {
            Glide
                .with(view.context)
                .load(url ?: "")
                .apply(RequestOptions()
                    .placeholder(R.drawable.ic_launcher_foreground))
                .into(view)
        }
    }

    private val representatives = mutableListOf<SingleRepresentative>()

    internal fun addRepresentatives(representatives: List<SingleRepresentative>) {
        this.representatives.addAll(representatives)

        notifyDataSetChanged()
    }

    inner class MyRepresentativesViewHolder(val binding: SingleRepresentativeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(representative: SingleRepresentative) {
            with(binding) {
                singleRepresentative = representative

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