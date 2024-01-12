package com.example.petfinderapp.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

abstract class BaseFragment : Fragment() {

    protected fun navigateToDirection(navDirections: NavDirections) {
        findNavController().navigate(navDirections)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
    }

    abstract fun setupObservers()

    abstract fun setupViews()
}