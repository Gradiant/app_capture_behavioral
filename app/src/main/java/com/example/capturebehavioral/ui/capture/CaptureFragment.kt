package com.example.capturebehavioral.ui.capture

import android.annotation.SuppressLint
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
import com.example.capturebehavioral.databinding.CaptureFragmentBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CaptureFragment: Fragment(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var linearAcelSensor: Sensor? = null
    private var acelSensor: Sensor? = null
    private var gyrosSensor: Sensor? = null


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
        sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
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