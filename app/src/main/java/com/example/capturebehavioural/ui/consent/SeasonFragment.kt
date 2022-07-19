package com.example.capturebehavioural.ui.consent

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.capturebehavioural.databinding.SeasonFragmentBinding
import com.example.capturebehavioural.ui.capture.CaptureActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SeasonFragment : Fragment() {

    private lateinit var binding: SeasonFragmentBinding
    private lateinit var viewModel: SeasonViewModel

    private lateinit var email: String

    private val args: SeasonFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SeasonFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = args.email

        viewModel = ViewModelProvider(
            this,
            SeasonViewModel.MainViewModelFactory()
        )[SeasonViewModel::class.java]

        viewModel.seasonState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> updateUI(state) }
            .launchIn(lifecycleScope)

        binding.btNewEmail.setOnClickListener {
            viewModel.clickNewUser()
        }

        binding.btNewSeason.setOnClickListener {
            viewModel.clickNewSeason()
        }
    }

    private fun updateUI(screenState: SeasonState?) {
        when (screenState) {
            is SeasonState.NewSeason -> {
                //val action = SeasonFragmentDirections.actionSeasonFragmentToCaptureActivity()
                //view?.findNavController()?.navigate(action)
                val intent = Intent(this.activity, CaptureActivity::class.java)
                intent.putExtra("email", email)
                intent.putExtra("season",resources.getString(R.string.sesion).lowercase() + "_" + binding.spinnerSesion.selectedItem)
                intent.putExtra("position", binding.spinnerPosition.selectedItemPosition.toString())

                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
                startActivity(intent)
            }
            is SeasonState.NewUser -> {
                val action = SeasonFragmentDirections.actionSeasonFragmentToRequestFragment()
                view?.findNavController()?.navigate(action)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onResume()
    }

}