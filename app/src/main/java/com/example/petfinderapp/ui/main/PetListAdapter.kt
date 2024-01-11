package com.example.petfinderapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.petfinderapp.R
import com.example.petfinderapp.databinding.PetListItemBinding
import com.example.petfinderapp.domain.models.AnimalDetails
import com.squareup.picasso.Picasso

class PetListAdapter :
    PagingDataAdapter<AnimalDetails, PetListAdapter.AnimalViewHolder>(AnimalDiffCallback()) {

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val binding =
            PetListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimalViewHolder(binding)
    }

    inner class AnimalViewHolder(private val binding: PetListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(animalDetails: AnimalDetails?) {
            val context = binding.root.context
            with(binding) {
                animalDetails?.let { animalDetails ->
                    nameTextView.text = context.getString(R.string.name, animalDetails.name)
                    genderTextView.text = context.getString(R.string.gender, animalDetails.gender)
                    animalDetails.photoUrl?.let {
                        Picasso.get().load(it).placeholder(R.drawable.image_not_available_icon)
                            .into(petPhotoImgView)
                    }
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