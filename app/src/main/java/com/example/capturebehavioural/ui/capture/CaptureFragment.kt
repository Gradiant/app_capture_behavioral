package com.example.capturebehavioural.ui.capture

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.capturebehavioural.R
import com.example.capturebehavioural.databinding.CaptureFragmentBinding
import com.example.capturebehavioural.ui.consent.ConsentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class CaptureFragment: Fragment(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var linearAcelSensor: Sensor? = null
    private var acelSensor: Sensor? = null
    private var gyrosSensor: Sensor? = null

    private var dialog: AlertDialog? = null

    private lateinit var binding: CaptureFragmentBinding
    private lateinit var viewModel: CaptureViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CaptureFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this,
            CaptureViewModel.MainViewModelFactory()
        )[CaptureViewModel::class.java]

        sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        viewModel.captureState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> updateUI(state) }
            .launchIn(lifecycleScope)

        binding.btStart.setOnClickListener {
            viewModel.clickStart()
        }

        binding.tbNext.setOnClickListener {
            viewModel.clickNext()
        }

        binding.btCapture.setOnClickListener {
            viewModel.clickCapture()
        }

        /* val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

         for (sensors in deviceSensors) {
             binding.tv.append(sensors.toString() + "\n\n")
         }*/

        linearAcelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        acelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyrosSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
/*
        if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            // Success! There's a magnetometer.
        } else {
            // Failure! No magnetometer.
        }*/
    }

    private fun updateUI(state: CaptureState?) {
        when(state) {
            is CaptureState.Next -> {
                binding.clDoc.visibility = View.GONE
                binding.clCapture.visibility = View.VISIBLE
            }
            is CaptureState.Start -> {
                binding.titlePrincipal.visibility = View.INVISIBLE
                binding.title.text = resources.getString(R.string.title_capture_two)

                binding.btStart.visibility = View.GONE
                binding.clDoc.visibility = View.VISIBLE
            }
            is CaptureState.Capture -> {
                binding.clCapture.visibility = View.GONE
                binding.btRecCapt.visibility = View.VISIBLE
            }
            is CaptureState.Dialog  -> {
                if(dialog == null || (dialog != null && !dialog!!.isShowing)) {
                    val view = layoutInflater.inflate(R.layout.dialog_politics, null)

                    val builder = AlertDialog.Builder(context)
                    builder.setPositiveButton(resources.getString(R.string.back_politics)) {
                                _, _ ->
                        dialog?.dismiss()
                            //val nameAudio = email + "_" + spSex.selectedItem.toString().lowercase() + "_" + resources.getString(R.string.sesion).lowercase() + "_" + spSesion.selectedItem
                        }

                    builder.setView(view)
                    dialog = builder.create()
                    dialog?.show()
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        //TODO: hacer un when para distinguir el tipo de evento
        event?.let { e ->
            val lux = e.values[0]
            Log.d("SENSOR_VALUE", lux.toString())
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //do nothing
    }


    override fun onResume() {
        super.onResume()
        linearAcelSensor?.also { light ->
            sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
        }
        acelSensor?.also { light ->
            sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
        }
        gyrosSensor?.also { light ->
            sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

}