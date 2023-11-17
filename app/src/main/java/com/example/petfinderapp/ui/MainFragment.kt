package com.example.petfinderapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.petfinderapp.R
import com.example.petfinderapp.databinding.FragmentAnimalsListBinding
import com.example.petfinderapp.ui.base.sharedFlowCollect
import com.example.petfinderapp.ui.base.showToast
import com.example.petfinderapp.ui.base.stateFlowCollect
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var _binding: FragmentAnimalsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModel()
    private val animalDetailsAdapter = AnimalsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimalsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        val animaTypesAdapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.pet_types, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        with(binding) {
            recyclerViewAnimals.adapter = animalDetailsAdapter
            selectorAnimalType.adapter = animaTypesAdapter
            selectorAnimalType.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    val selectedPetType =
                        if (position == 0) SelectedPetType.DOG else SelectedPetType.CAT

                    animalDetailsAdapter.clearAnimalsList()
                    loadingIndicator.isVisible = true
                    viewModel.checkTokenAndGetListOfAnimals(selectedPetType)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
        }
    }

    private fun setupObservers() {
        stateFlowCollect(viewModel.animalsList) {
            binding.loadingIndicator.isVisible = false
            animalDetailsAdapter.updateAnimalsList(it)
        }

        sharedFlowCollect(viewModel.errorMessage) { networkError ->
            binding.loadingIndicator.isVisible = false
            showToast(networkError)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}