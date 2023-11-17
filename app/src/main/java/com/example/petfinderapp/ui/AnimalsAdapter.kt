package com.example.petfinderapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.petfinderapp.R
import com.example.petfinderapp.databinding.AnimalDetailsItemBinding
import com.example.petfinderapp.domain.models.AnimalDetails

class AnimalsAdapter : RecyclerView.Adapter<AnimalsAdapter.AnimalViewHolder>() {

    private var animalsDetailsList: List<AnimalDetails> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun updateAnimalsList(animalDetailsList: List<AnimalDetails>) {
        this.animalsDetailsList = animalDetailsList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearAnimalsList() {
        this.animalsDetailsList = emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val binding =
            AnimalDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimalViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return animalsDetailsList.size
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.bind(animalsDetailsList[position])
    }

    inner class AnimalViewHolder(private val binding: AnimalDetailsItemBinding) : ViewHolder(binding.root) {

        fun bind(animalDetails: AnimalDetails) {
            val context = binding.root.context
            with(binding) {
                nameTextView.text = context.getString(R.string.name, animalDetails.name)
                genderTextView.text = context.getString(R.string.gender, animalDetails.gender)
                sizeTextView.text = context.getString(R.string.size, animalDetails.size)
                breedTextView.text = context.getString(R.string.breed, animalDetails.breed)
                statusTextView.text = context.getString(R.string.status, animalDetails.status)
                distanceTextView.text = context.getString(R.string.distance, animalDetails.distance)
            }
        }
    }
}