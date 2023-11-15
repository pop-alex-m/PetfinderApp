package com.example.petfinderapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petfinderapp.databinding.FragmentAnimalsListBinding
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

        binding.animalsRecyclerView.adapter = animalDetailsAdapter
        binding.buttonGetAnimals.setOnClickListener {
            viewModel.getListOfPets()
        }

        stateFlowCollect(viewModel.animalsList) {
            animalDetailsAdapter.updateAnimalsList(it)
        }

        sharedFlowCollect(viewModel.networkError) { networkError ->
            networkError?.let {
                showToast(it.errorMessage)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}