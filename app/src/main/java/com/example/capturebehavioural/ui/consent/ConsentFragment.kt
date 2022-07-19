package com.example.capturebehavioural.ui.consent

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.capturebehavioural.R
import com.example.capturebehavioural.databinding.ConsentFragmentBinding
import com.example.domain.Response
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ConsentFragment: Fragment() {

    private lateinit var binding: ConsentFragmentBinding
    private lateinit var viewModel: ConsentViewModel
    private var consent = 0

    private lateinit var email: String
    private val args: ConsentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ConsentFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = args.email

        viewModel = ViewModelProvider(this,
            ConsentViewModel.MainViewModelFactory()
        )[ConsentViewModel::class.java]

        lifecycleScope.launch {
            viewModel.consentState.collect {
                updateUI(it)
            }
        }
        viewModel.responseState
            .flowWithLifecycle(lifecycle,  Lifecycle.State.STARTED)
            .onEach { state -> updateUser(state) }
            .launchIn(lifecycleScope)

        binding.nextConsent.setOnClickListener {
            if(!binding.editTextName.text.isNullOrEmpty() and !binding.editTextLastname.text.isNullOrEmpty()) {
                viewModel.onButtonClicked(consent,  binding.cbConsent1.isChecked, binding.cbConsent2.isChecked, binding.cbConsent3.isChecked,  binding.cbConsent4.isChecked,  binding.cbConsent5.isChecked)
            } else {
                showError(resources.getString(R.string.error_empty))
            }
        }
    }

    private fun updateUser(screenState: Response<Any>?) {
        when(screenState) {
            is Response.Error -> {
                //do nothing
            }
            is Response.Success -> {
                val action = ConsentFragmentDirections.actionConsentFragmentToSeasonFragment(email)
                view?.findNavController()?.navigate(action)
            }
        }
    }

    private fun updateUI(screenState: ConsentState?) {
        when (screenState) {
            is ConsentState.InfoOK -> {
                consent = screenState.consent
                binding.clInfo.visibility = View.INVISIBLE
                binding.cbConsent1.visibility = View.VISIBLE
            }
            is ConsentState.Consent1 -> {
                consent = screenState.consent
                binding.cbConsent1.visibility = View.GONE
                binding.cbConsent2.visibility = View.VISIBLE
            }
            is ConsentState.Consent2 -> {
                consent = screenState.consent
                binding.cbConsent2.visibility = View.GONE
                binding.cbConsent3.visibility = View.VISIBLE
            }
            is ConsentState.Consent3 -> {
                consent = screenState.consent
                binding.cbConsent3.visibility = View.GONE
                binding.cbConsent4.visibility = View.VISIBLE
            }
            is ConsentState.Consent4 -> {
                consent = screenState.consent
                binding.cbConsent4.visibility = View.GONE
                binding.cbConsent5.visibility = View.VISIBLE
            }
            is ConsentState.AllConsentOk -> {
                viewModel.sendUserConsent(binding.editTextName.text.toString(), binding.editTextLastname.text.toString(), binding.editTextAddress.text.toString(),
                binding.editTextPhone.text.toString(), email)
            }
            is ConsentState.Error -> showError(resources.getString(R.string.error_consent))
        }
    }


    private fun showError(error: String) {
        val builder = AlertDialog.Builder(context)
            .setMessage(error)
        builder.show()
    }
}