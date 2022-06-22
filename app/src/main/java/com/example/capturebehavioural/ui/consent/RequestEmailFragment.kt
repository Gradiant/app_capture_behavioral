package com.example.capturebehavioural.ui.consent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.capturebehavioural.R
import com.example.capturebehavioural.databinding.RequestEmailFragmentBinding
import com.example.domain.Response
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RequestEmailFragment : Fragment() {

    private lateinit var binding: RequestEmailFragmentBinding
    private lateinit var viewModel: RequestEmailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RequestEmailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            RequestEmailViewModel.MainViewModelFactory()
        )[RequestEmailViewModel::class.java]

        lifecycleScope.launch {
            viewModel.requestEmailState.collect {
                updateUI(it)
            }
        }

        viewModel.responseState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> updateUser(state) }
            .launchIn(lifecycleScope)

        binding.btConfirmEmail.setOnClickListener {
            viewModel.consentAcept(binding.editTextEmail.text.toString())
        }
    }

    private fun updateUser(screenState: Response<Any>?) {
        when (screenState) {
            is Response.Error -> {
                //do nothing
            }
            is Response.Success -> {
                viewModel.confirmEmail(
                    screenState.data as List<String>,
                    binding.editTextEmail.text.toString()
                )
            }
        }
    }

    private fun hideKeyBoard(v: View) {
        val imm: InputMethodManager =
            context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    private fun updateUI(screenState: RequestEmailState?) {
        when (screenState) {
            is RequestEmailState.NameError -> showMessageError(getString(R.string.email_empty_error))
            is RequestEmailState.FormatError -> showMessageError(getString(R.string.email_error))
            is RequestEmailState.EmailRegistered -> {
                val action =
                    RequestEmailFragmentDirections.actionRequestFragmentToCaptureFragment(binding.editTextEmail.text.toString())
                view?.findNavController()?.navigate(action)
            }
            is RequestEmailState.NewEmail -> {
                val action =
                    RequestEmailFragmentDirections.actionRequestFragmentToConsentDetailFragment(binding.editTextEmail.text.toString())
                view?.findNavController()?.navigate(action)
            }
        }
    }

    private fun showMessageError(message: String) {
        binding.editTextEmail.error = message
    }
}
