package com.example.petfinderapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.petfinderapp.R
import com.example.petfinderapp.databinding.FragmentPetDetailsBinding
import com.example.petfinderapp.domain.models.PetDetails
import com.example.petfinderapp.ui.base.BaseFragment
import com.example.petfinderapp.ui.base.stateFlowCollect
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class PetDetailsFragment : BaseFragment() {

    private var _binding: FragmentPetDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PetDetailsViewModel by viewModel()

    private val petDetailsFragmentArgs: PetDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onPetDetails(petDetailsFragmentArgs.selectedPet)
    }

    override fun setupObservers() {
        stateFlowCollect(viewModel.animalDetails) {
            loadPetDetails(it)
        }
    }

    override fun setupViews() {}

    private fun loadPetDetails(petDetails: PetDetails?) {
        petDetails?.let {
            with(binding) {
                nameTextView.text = context?.getString(R.string.name, it.name)
                genderTextView.text = context?.getString(R.string.gender, it.gender)
                sizeTextView.text = context?.getString(R.string.size, it.size)
                breedTextView.text = context?.getString(R.string.breed, it.breed)
                statusTextView.text = context?.getString(R.string.status, it.status)
                distanceTextView.text = context?.getString(R.string.distance, it.distance)
                it.smallPhotoUrl?.let { url ->
                    Picasso.get().load(url).placeholder(R.drawable.image_not_available_icon)
                        .into(petPhotoImgView)
                }
            }
        }
    }
}