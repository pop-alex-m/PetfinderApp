package com.example.petfinderapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.petfinderapp.domain.models.AnimalDetails
import com.example.petfinderapp.databinding.AnimalDetailsItemBinding

class AnimalsAdapter : RecyclerView.Adapter<AnimalsAdapter.AnimalViewHolder>(){

    private var animalsDetailsList : List<AnimalDetails> = emptyList()

    fun updateAnimalsList(animalDetailsList: List<AnimalDetails>) {
        this.animalsDetailsList = animalDetailsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val binding = AnimalDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimalViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return animalsDetailsList.size
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.bind(animalsDetailsList[position])
    }

    inner class AnimalViewHolder(private val binding: AnimalDetailsItemBinding) : ViewHolder(binding.root) {

        fun bind(item: AnimalDetails) {
            with(binding) {
                nameTextView.text = "Name : " + item.name
                genderTextView.text = "Gender : " + item.gender
                sizeTextView.text = "Size : " + item.size
                breedTextView.text = "Breed : " + item.breed
                statusTextView.text = "Status : " + item.status
                distanceTextView.text = "Distance : " + item.distance
            }
        }
    }
}