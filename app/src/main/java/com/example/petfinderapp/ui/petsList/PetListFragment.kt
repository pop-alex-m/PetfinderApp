package com.example.petfinderapp.ui.petsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.example.petfinderapp.R
import com.example.petfinderapp.databinding.FragmentPetListBinding
import com.example.petfinderapp.domain.models.SelectedPetType
import com.example.petfinderapp.ui.base.BaseFragment
import com.example.petfinderapp.ui.base.sharedFlowCollect
import com.example.petfinderapp.ui.base.showToast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PetListFragment : BaseFragment() {

    private var _binding: FragmentPetListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PetListViewModel by viewModel()

    private val animalDetailsAdapter = PetListAdapter { selectedAnimalDetails ->
        navigateToDirection(
            PetListFragmentDirections.actionPetListToDetailsScreen(
                selectedAnimalDetails
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupViews() {
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
                    animalDetailsAdapter.submitData(lifecycle, PagingData.empty())
                    loadingIndicator.isVisible = true

                    val selectedPetType =
                        if (position == 0) SelectedPetType.DOG else SelectedPetType.CAT
                    onLoadPetList(selectedPetType)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
        }
    }

    override fun setupObservers() {
        sharedFlowCollect(viewModel.errorMessage) { networkError ->
            binding.loadingIndicator.isVisible = false
            showToast(networkError)
        }
        lifecycleScope.launch {
            animalDetailsAdapter.loadStateFlow.collectLatest { loadState ->
                (loadState.refresh as? LoadState.Error)?.let { error ->
                    viewModel.onError(error.error)
                }
            }
        }
    }

    private fun onLoadPetList(selectedPetType: SelectedPetType) {
        lifecycleScope.launch {
            viewModel.getListOfAnimals(selectedPetType).collectLatest {
                binding.loadingIndicator.isVisible = false
                animalDetailsAdapter.submitData(lifecycle, it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}