package com.example.petfinderapp.ui.petsList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.petfinderapp.R
import com.example.petfinderapp.databinding.PetListItemBinding
import com.example.petfinderapp.domain.models.PetDetails
import com.squareup.picasso.Picasso

class PetListAdapter(private val onPetItemClick: (PetDetails) -> Unit) :
    PagingDataAdapter<PetDetails, PetListAdapter.AnimalViewHolder>(AnimalDiffCallback()) {

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

        fun bind(petDetails: PetDetails?) {
            with(binding) {
                val context = root.context
                petDetails?.let { animalDetails ->
                    nameTextView.text = context.getString(R.string.name, animalDetails.name)
                    genderTextView.text = context.getString(R.string.gender, animalDetails.gender)
                    animalDetails.smallPhotoUrl?.let {
                        Picasso.get().load(it).placeholder(R.drawable.image_not_available_icon)
                            .into(petPhotoImgView)
                    }
                    root.setOnClickListener {
                        onPetItemClick(animalDetails)
                    }
                }
            }
        }
    }

    class AnimalDiffCallback : DiffUtil.ItemCallback<PetDetails>() {
        override fun areItemsTheSame(oldItem: PetDetails, newItem: PetDetails): Boolean {
            return oldItem.name == newItem.name && oldItem.breed == newItem.breed
        }

        override fun areContentsTheSame(oldItem: PetDetails, newItem: PetDetails): Boolean {
            return oldItem == newItem
        }
    }
}