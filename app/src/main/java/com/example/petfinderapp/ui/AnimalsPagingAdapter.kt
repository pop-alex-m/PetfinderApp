package com.example.petfinderapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.petfinderapp.R
import com.example.petfinderapp.databinding.AnimalDetailsItemBinding
import com.example.petfinderapp.domain.models.AnimalDetails

class AnimalsPagingAdapter :
    PagingDataAdapter<AnimalDetails, AnimalsPagingAdapter.AnimalViewHolder>(AnimalDiffCallback()) {

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val binding =
            AnimalDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimalViewHolder(binding)
    }

    inner class AnimalViewHolder(private val binding: AnimalDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(animalDetails: AnimalDetails?) {
            val context = binding.root.context
            with(binding) {
                animalDetails?.let { animalDetails ->
                    nameTextView.text = context.getString(R.string.name, animalDetails.name)
                    genderTextView.text = context.getString(R.string.gender, animalDetails.gender)
                    sizeTextView.text = context.getString(R.string.size, animalDetails.size)
                    breedTextView.text = context.getString(R.string.breed, animalDetails.breed)
                    statusTextView.text = context.getString(R.string.status, animalDetails.status)
                    distanceTextView.text =
                        context.getString(R.string.distance, animalDetails.distance)
                }
            }
        }
    }

    class AnimalDiffCallback : DiffUtil.ItemCallback<AnimalDetails>() {
        override fun areItemsTheSame(oldItem: AnimalDetails, newItem: AnimalDetails): Boolean {
            return oldItem.name == newItem.name && oldItem.breed == newItem.breed
        }

        override fun areContentsTheSame(oldItem: AnimalDetails, newItem: AnimalDetails): Boolean {
            return oldItem == newItem
        }
    }


}