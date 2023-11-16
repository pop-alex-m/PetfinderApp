package com.example.petfinderapp.ui.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun <T> Fragment.stateFlowCollect(
    stateFlow: StateFlow<T>,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(state) {
            stateFlow.collect(block)
        }
    }
}

fun Fragment.showToast(message:String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun <T> Fragment.sharedFlowCollect(
    sharedFlow: SharedFlow<T>,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(state) {
            sharedFlow.collect(block)
        }
    }
}