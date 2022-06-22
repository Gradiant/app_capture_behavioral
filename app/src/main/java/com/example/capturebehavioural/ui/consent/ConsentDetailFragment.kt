package com.example.capturebehavioural.ui.consent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.capturebehavioural.databinding.ConsentDetailFragmentBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ConsentDetailFragment : Fragment() {

    private lateinit var binding: ConsentDetailFragmentBinding
    private lateinit var viewModel: ConsentDetailViewModel

    private var email = ""
    private val args: ConsentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ConsentDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = args.email

        viewModel = ViewModelProvider(this,
            ConsentDetailViewModel.MainViewModelFactory()
        )[ConsentDetailViewModel::class.java]

        lifecycleScope.launch {
            viewModel.consentDetailState.collect {
                updateUI(it)
            }
        }

        binding.btNext.setOnClickListener {
            viewModel.onButtonClicked()
        }
    }

    private fun updateUI(screenState: ConsentDetailState?) {
        when (screenState) {
            is ConsentDetailState.Next -> {
                val action = ConsentDetailFragmentDirections.actionConsentDetailFragmentToConsentFragment(email)
                view?.findNavController()?.navigate(action)
            }
        }
    }
}