package com.example.capturebehavioural.ui.capture

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capturebehavioural.R
import com.example.capturebehavioural.databinding.CaptureFragmentBinding
import com.example.domain.ListItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.io.FileWriter
import android.view.*

@ExperimentalCoroutinesApi
class CaptureFragment: Fragment(), SensorEventListener, ItemArrayAdapter.ListItemClickListener {

    private lateinit var sensorManager: SensorManager
    private var linearAcelSensor: Sensor? = null
    private var acelSensor: Sensor? = null
    private var gyrosSensor: Sensor? = null
    private var magSensor: Sensor? = null

    private var dialog: AlertDialog? = null

    private var itemList = ArrayList<ListItem>()
    private lateinit var itemArrayAdapter : ItemArrayAdapter

    private lateinit var binding: CaptureFragmentBinding
    private lateinit var viewModel: CaptureViewModel

    private lateinit var email: String
    private lateinit var season: String
    private lateinit var position: String

    private val accFile: File = File.createTempFile("Accelerometer", "csv")
    private var fileWriterAc: FileWriter = FileWriter(accFile)

    private val linearAccFile: File = File.createTempFile("LinearAccelerometer", "csv")
    var fileWriterLinearAc: FileWriter = FileWriter(linearAccFile)

    private val gyrosFile: File = File.createTempFile("Gyroscrope", "csv")
    var fileWriterGyros: FileWriter = FileWriter(gyrosFile)

    private val magFile: File = File.createTempFile("Magnetometer", "csv")
    var fileWriterMag: FileWriter = FileWriter(magFile)


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

        email = activity?.intent?.getStringExtra("email") ?: ""
        season = activity?.intent?.getStringExtra("season") ?: ""
        position = activity?.intent?.getStringExtra("position") ?: "0"

        sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        viewModel.captureState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> updateUI(state) }
            .launchIn(lifecycleScope)

        binding.btStart.setOnClickListener {
            viewModel.clickStart()
        }

        binding.btFinish.setOnClickListener {
            viewModel.clickFinish()
        }

        binding.btNext.setOnClickListener {
            viewModel.clickNext()
        }

        binding.btCapture.setOnClickListener {
            viewModel.clickCapture()
        }

        binding.btRecCapt.setOnClickListener {
            viewModel.clickRecCapt()
        }

        binding.cbAccept.setOnClickListener {
            viewModel.clickAcept(binding.cbAccept.isChecked)
        }

        binding.btOkResults.setOnClickListener {
            viewModel.clickOkResults()
        }

        binding.tvInfo.setOnClickListener {
            viewModel.showDialog()
        }

        binding.btCont.setOnClickListener {
            viewModel.clickCont()
        }

        binding.rvForm.isNestedScrollingEnabled = false

        context?.let {
            itemArrayAdapter = ItemArrayAdapter(this, context!!)

            binding.rvForm.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
            binding.rvForm.adapter = itemArrayAdapter

            itemList = arrayListOf(ListItem(resources.getString(R.string.askOne),arrayListOf(resources.getString(R.string.responseOne_One),resources.getString(R.string.responseOne_OK),
                resources.getString(R.string.responseOne_Three),resources.getString(R.string.responseOne_Four)), arrayListOf(resources.getString(R.string.responseOne_OK))),
            ListItem(resources.getString(R.string.askTwo),arrayListOf(resources.getString(R.string.responseTwo_One),resources.getString(R.string.responseTwo_Two),
                resources.getString(R.string.responseTwo_OK),resources.getString(R.string.responseTwo_Four)), arrayListOf(resources.getString(R.string.responseTwo_OK))),
            ListItem(resources.getString(R.string.askThree),arrayListOf(resources.getString(R.string.responseThree_One),resources.getString(R.string.responseThree_Ok),
                resources.getString(R.string.responseThree_Three),resources.getString(R.string.responseThree_Four)), arrayListOf(resources.getString(R.string.responseThree_Ok))),
            ListItem(resources.getString(R.string.askFour),arrayListOf(resources.getString(R.string.responseFour_OkOne),resources.getString(R.string.responseFour_OkTwo)), arrayListOf(resources.getString(R.string.responseFour_OkOne), resources.getString(R.string.responseFour_OkTwo))),
            ListItem(resources.getString(R.string.askFive),arrayListOf(resources.getString(R.string.responseFive_One),resources.getString(R.string.responseFive_Two),
                resources.getString(R.string.responseFive_Three),resources.getString(R.string.responseFive_OK)),arrayListOf(resources.getString(R.string.responseFive_OK))),
            ListItem(resources.getString(R.string.askSix),arrayListOf(resources.getString(R.string.responseSix_Ok)), arrayListOf(resources.getString(R.string.responseSix_Ok))),
            ListItem(resources.getString(R.string.askSeven),arrayListOf(resources.getString(R.string.responseSeven_One),resources.getString(R.string.responseSeven_Two),
                resources.getString(R.string.responseSeven_Ok),resources.getString(R.string.responseSeven_Four)), arrayListOf(resources.getString(R.string.responseSeven_Ok))),
            ListItem(resources.getString(R.string.askEight),arrayListOf(resources.getString(R.string.responseEight_Ok)), arrayListOf(resources.getString(R.string.responseEight_Ok))),
            ListItem(resources.getString(R.string.askNine),arrayListOf(resources.getString(R.string.responseNine_One),resources.getString(R.string.responseNine_Two),
                resources.getString(R.string.responseNine_Three),resources.getString(R.string.responseNine_Ok)), arrayListOf(resources.getString(R.string.responseNine_Ok))),
            ListItem(resources.getString(R.string.askTen),arrayListOf(resources.getString(R.string.responseTen_One),resources.getString(R.string.responseTen_OK),
                resources.getString(R.string.responseTen_Three),resources.getString(R.string.responseTen_Four)), arrayListOf(resources.getString(R.string.responseTen_OK))),
            ListItem(resources.getString(R.string.askEleven),arrayListOf(resources.getString(R.string.responseEleven_One),resources.getString(R.string.responseEleven_Ok),
                resources.getString(R.string.responseEleven_Three),resources.getString(R.string.responseEleven_Four)), arrayListOf(resources.getString(R.string.responseEleven_Ok))),
            ListItem(resources.getString(R.string.askTwelve),arrayListOf(resources.getString(R.string.responseTwelve_OK),resources.getString(R.string.responseTwelve_Two),
                resources.getString(R.string.responseTwelve_Three),resources.getString(R.string.responseTwelve_Four)), arrayListOf(resources.getString(R.string.responseTwelve_OK))),
            ListItem(resources.getString(R.string.askThirteen),arrayListOf(resources.getString(R.string.responseThirteen_Ok)), arrayListOf(resources.getString(R.string.responseThree_Ok))),
            ListItem(resources.getString(R.string.askFourteen),arrayListOf(resources.getString(R.string.responseFourteen_One),resources.getString(R.string.responseFourteen_Two),
                    resources.getString(R.string.responseFourteen_Three),resources.getString(R.string.responseFourteen_Ok)), arrayListOf(resources.getString(R.string.responseFourteen_Ok))),
            ListItem(resources.getString(R.string.askFifteen),arrayListOf(resources.getString(R.string.responseFifteen_Ok)), arrayListOf(resources.getString(R.string.responseFifteen_Ok))),
            ListItem(resources.getString(R.string.askSixteen),arrayListOf(resources.getString(R.string.responseSixteen_One),resources.getString(R.string.responseSixteen_Two),
                resources.getString(R.string.responseSixteen_Ok),resources.getString(R.string.responseSixteen_Four)), arrayListOf(resources.getString(R.string.responseSixteen_Ok))),
            ListItem(resources.getString(R.string.askSeventeen),arrayListOf(resources.getString(R.string.responseSeventeen_One)), arrayListOf(resources.getString(R.string.responseSeven_Ok))),
            ListItem(resources.getString(R.string.askEighteen),arrayListOf(resources.getString(R.string.responseEighteen_OK)), arrayListOf(resources.getString(R.string.responseEighteen_OK))),
            ListItem(resources.getString(R.string.askNighteen),arrayListOf(resources.getString(R.string.responseNighteen_One),resources.getString(R.string.responseNighteen_Two),
                resources.getString(R.string.responseNighteen_Three),resources.getString(R.string.responseNighteen_Ok)), arrayListOf(resources.getString(R.string.responseNighteen_Ok))),
            ListItem(resources.getString(R.string.askTwenty),arrayListOf(resources.getString(R.string.responseTwenty_Ok)), arrayListOf(resources.getString(R.string.responseTwenty_Ok))),
            ListItem(resources.getString(R.string.askTwentyOne),arrayListOf(resources.getString(R.string.responseTwentyOne_Ok)), arrayListOf(resources.getString(R.string.responseTwentyOne_Ok))),
            ListItem(resources.getString(R.string.askTwentyTwo),arrayListOf(resources.getString(R.string.responseTwentyTwo_Ok),resources.getString(R.string.responseTwentyTwo_Two),
                resources.getString(R.string.responseTwentyTwo_Three),resources.getString(R.string.responseTwentyTwo_Four)), arrayListOf(resources.getString(R.string.responseTwentyTwo_Ok))),
            ListItem(resources.getString(R.string.askTwentyThree),arrayListOf(resources.getString(R.string.responseTwentyThree_One),resources.getString(R.string.responseTwentyThree_Two),
                resources.getString(R.string.responseTwentyThree_Ok),resources.getString(R.string.responseTwentyThree_Four)), arrayListOf(resources.getString(R.string.responseTwentyThree_Ok))),
            ListItem(resources.getString(R.string.askTwentyFour),arrayListOf(resources.getString(R.string.responseTwentyFour_Ok)), arrayListOf(resources.getString(R.string.responseTwentyFour_Ok))),
            ListItem(resources.getString(R.string.askTwentyFive),arrayListOf(resources.getString(R.string.responseTwentyFive_One),resources.getString(R.string.responseTwentyFive_Two),
                resources.getString(R.string.responseTwentyFive_Three),resources.getString(R.string.responseTwentyFive_OK)), arrayListOf(resources.getString(R.string.responseTwentyFive_OK))),
            ListItem(resources.getString(R.string.askTwentySix),arrayListOf(resources.getString(R.string.responseTwentySix_One),resources.getString(R.string.responseTwentySix_Ok),
                resources.getString(R.string.responseTwentySix_Three),resources.getString(R.string.responseTwentySix_Four)), arrayListOf(resources.getString(R.string.responseTwentySix_Ok))),
            ListItem(resources.getString(R.string.askTwentySeven),arrayListOf(resources.getString(R.string.responseTwentySeven_Ok)),
                arrayListOf(resources.getString(R.string.responseTwentySeven_Ok))),
            ListItem(resources.getString(R.string.askTwentyEight),arrayListOf(resources.getString(R.string.responseTwentyEight_Ok)),
                arrayListOf(resources.getString(R.string.responseTwentyEight_Ok))),
            ListItem(resources.getString(R.string.askTwentyNine),arrayListOf(resources.getString(R.string.responseTwentyNine_OK)), arrayListOf(resources.getString(R.string.responseTwentyNine_OK))),
            ListItem(resources.getString(R.string.askThirty),arrayListOf(resources.getString(R.string.responseThirty_One),resources.getString(R.string.responseThirty_Two),
                resources.getString(R.string.responseThirty_Ok),resources.getString(R.string.responseThirty_Four)), arrayListOf(resources.getString(R.string.responseThirty_Ok))))

            itemArrayAdapter.submitList(itemList)

        }

        linearAcelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        acelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyrosSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    private fun updateUI(state: CaptureState?) {
        when(state) {
            is CaptureState.Next -> {
                binding.title.visibility = View.INVISIBLE
                binding.titlePrincipal.text = resources.getString(R.string.results)
                binding.tvResultOne.visibility = View.VISIBLE
                binding.tvResultTwo.visibility = View.VISIBLE
                binding.tvResultThree.visibility = View.VISIBLE
                binding.tvResultFour.visibility = View.VISIBLE
                binding.tvResultFive.visibility = View.VISIBLE

                binding.clDoc.visibility = View.GONE

                when (binding.spinnerOne.selectedItem.toString()) {
                    resources.getString(R.string.tree) -> binding.tvResultOne.text = resources.getString(R.string.tree_solution)
                    resources.getString(R.string.lips) -> binding.tvResultOne.text = resources.getString(R.string.lips_solution)
                    resources.getString(R.string.root) -> binding.tvResultOne.text = resources.getString(R.string.root_solution)
                }

                when (binding.spinnerTwo.selectedItem.toString()) {
                    resources.getString(R.string.boat) -> binding.tvResultTwo.text = resources.getString(R.string.boat_solution)
                    resources.getString(R.string.alligator) -> binding.tvResultTwo.text = resources.getString(R.string.alligator_solution)
                }

                when (binding.spinnerThree.selectedItem.toString()) {
                    resources.getString(R.string.profile) -> binding.tvResultThree.text = resources.getString(R.string.profile_solution)
                    resources.getString(R.string.chandelier) -> binding.tvResultThree.text = resources.getString(R.string.chandelier_solution)
                }

                when (binding.spinnerFour.selectedItem.toString()) {
                    resources.getString(R.string.cliff) -> binding.tvResultFour.text = resources.getString(R.string.cliff_solution)
                    resources.getString(R.string.cat) -> binding.tvResultFour.text = resources.getString(R.string.cat_solution)
                    resources.getString(R.string.face) -> binding.tvResultFour.text = resources.getString(R.string.face_solution)
                }

                when (binding.spinnerFive.selectedItem.toString()) {
                    resources.getString(R.string.explosion) -> binding.tvResultFive.text = resources.getString(R.string.explosion_solution)
                    resources.getString(R.string.partner) -> binding.tvResultFive.text = resources.getString(R.string.partner_solution)
                }

                binding.btOkResults.visibility = View.VISIBLE

            }
            is CaptureState.ResultsOk -> {
                binding.titlePrincipal.visibility = View.GONE
                binding.title.visibility = View.VISIBLE
                binding.title.text = resources.getString(R.string.title_capture_four)
                binding.scroll.smoothScrollTo(0,0)

                binding.tvResultOne.visibility = View.GONE
                binding.tvResultTwo.visibility = View.GONE
                binding.tvResultThree.visibility = View.GONE
                binding.tvResultFour.visibility = View.GONE
                binding.tvResultFive.visibility = View.GONE
                binding.btOkResults.visibility = View.GONE

                binding.clCapture.visibility = View.VISIBLE
            }
            is CaptureState.Start -> {
                binding.titlePrincipal.text = resources.getString(R.string.title_capture_three)

                binding.btStart.visibility = View.GONE
                binding.clDoc.visibility = View.VISIBLE
            }
            is CaptureState.Capture -> {
                binding.title.text = resources.getString(R.string.title_capture_five)
                binding.clCapture.visibility = View.GONE
                binding.btRecCapt.visibility = View.VISIBLE
            }
            is CaptureState.RecCap -> {
                binding.title.text = resources.getString(R.string.title_capture_four_two)
                binding.clCapture.visibility = View.VISIBLE
                binding.btRecCapt.visibility = View.GONE
            }
            is CaptureState.Checked -> {
                binding.btNext.isEnabled = state.isChecked
            }
            is CaptureState.Form -> {
                binding.title.visibility = View.INVISIBLE
                binding.titlePrincipal.text = resources.getString(R.string.title_capture_two)
                binding.btStart.visibility = View.VISIBLE
                binding.rvForm.visibility = View.GONE
                binding.btCont.visibility = View.GONE
            }
            is CaptureState.Dialog  -> {
                if(dialog == null || (dialog != null && !dialog!!.isShowing)) {
                    val view = layoutInflater.inflate(R.layout.dialog_politics, null)

                    val builder = AlertDialog.Builder(context)
                    builder.setPositiveButton(resources.getString(R.string.back_politics)) {
                                _, _ ->
                        dialog?.dismiss()
                    }
                    builder.setView(view)
                    dialog = builder.create()
                    dialog?.show()
                }
            }
            is CaptureState.Finish -> {
                binding.title.text = resources.getString(R.string.title_capture_six)
                binding.btRecCapt.visibility = View.GONE
                binding.clCapture.visibility = View.GONE
                binding.btFinish.visibility = View.VISIBLE
            }
            is CaptureState.Navigation -> {
                fileWriterAc.close()
                fileWriterGyros.close()
                fileWriterLinearAc.close()
                viewModel.saveValues(email, season, "Accelerometer", accFile)
                viewModel.saveValues(email, season, "LinearAccelerometer", linearAccFile)
                viewModel.saveValues(email, season, "Gyroscrope", gyrosFile)
                viewModel.saveValues(email, season, "Magnetometer", magFile)


                activity?.finish()
            }
            else ->  {
                //do nothing
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let { e ->
            when(e.sensor.type) {
                TYPE_LINEAR_ACCELERATION -> {
                    try {
                        fileWriterLinearAc.append(System.currentTimeMillis().toString())
                        fileWriterLinearAc.append(',')
                        fileWriterLinearAc.append(e.timestamp.toString())
                        fileWriterLinearAc.append(',')
                        fileWriterLinearAc.append(e.values[0].toString())
                        fileWriterLinearAc.append(',')
                        fileWriterLinearAc.append(e.values[1].toString())
                        fileWriterLinearAc.append(',')
                        fileWriterLinearAc.append(e.values[2].toString())
                        fileWriterLinearAc.append(',')
                        fileWriterLinearAc.append(position)
                        fileWriterLinearAc.append(',')
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            activity?.display?.let {
                                fileWriterLinearAc.append(it.rotation.toString())
                            }
                        } else {
                            @Suppress("DEPRECATION")
                            activity?.windowManager?.defaultDisplay?.let {
                                fileWriterLinearAc.append(it.rotation.toString())
                            }
                        }
                        fileWriterLinearAc.append('\n')

                        println("Write CSV successfully!")

                    } catch (e: Exception) {
                        println("Writing CSV error!")
                        e.printStackTrace()
                    }
                }
                TYPE_ACCELEROMETER -> {
                    try {
                        fileWriterAc.append(System.currentTimeMillis().toString())
                        fileWriterAc.append(',')
                        fileWriterAc.append(e.timestamp.toString())
                        fileWriterAc.append(',')
                        fileWriterAc.append(e.values[0].toString())
                        fileWriterAc.append(',')
                        fileWriterAc.append(e.values[1].toString())
                        fileWriterAc.append(',')
                        fileWriterAc.append(e.values[2].toString())
                        fileWriterAc.append(',')
                        fileWriterAc.append(position)
                        fileWriterAc.append(',')
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            activity?.display?.let {
                                fileWriterAc.append(it.rotation.toString())
                            }
                        } else {
                            @Suppress("DEPRECATION")
                            activity?.windowManager?.defaultDisplay?.let {
                                fileWriterAc.append(it.rotation.toString())
                            }
                        }
                        fileWriterAc.append('\n')

                        println("Write CSV successfully!")

                    } catch (e: Exception) {
                        println("Writing CSV error!")
                        e.printStackTrace()
                    }                }
                TYPE_GYROSCOPE -> {
                    try {
                        fileWriterGyros.append(System.currentTimeMillis().toString())
                        fileWriterGyros.append(',')
                        fileWriterGyros.append(e.timestamp.toString())
                        fileWriterGyros.append(',')
                        fileWriterGyros.append(e.values[0].toString())
                        fileWriterGyros.append(',')
                        fileWriterGyros.append(e.values[1].toString())
                        fileWriterGyros.append(',')
                        fileWriterGyros.append(e.values[2].toString())
                        fileWriterGyros.append(',')
                        fileWriterGyros.append(position)
                        fileWriterGyros.append(',')
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            activity?.display?.let {
                                fileWriterGyros.append(it.rotation.toString())
                            }
                        } else {
                            @Suppress("DEPRECATION")
                            activity?.windowManager?.defaultDisplay?.let {
                                fileWriterGyros.append(it.rotation.toString())
                            }
                        }
                        fileWriterGyros.append('\n')

                        println("Write CSV successfully!")

                    } catch (e: Exception) {
                        println("Writing CSV error!")
                        e.printStackTrace()
                    }
                }
                TYPE_MAGNETIC_FIELD -> {
                    try {
                        fileWriterMag.append(System.currentTimeMillis().toString())
                        fileWriterMag.append(',')
                        fileWriterMag.append(e.timestamp.toString())
                        fileWriterMag.append(',')
                        fileWriterMag.append(e.values[0].toString())
                        fileWriterMag.append(',')
                        fileWriterMag.append(e.values[1].toString())
                        fileWriterMag.append(',')
                        fileWriterMag.append(e.values[2].toString())
                        fileWriterMag.append(',')
                        fileWriterMag.append(position)
                        fileWriterMag.append(',')
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            activity?.display?.let {
                                fileWriterMag.append(it.rotation.toString())
                            }
                        } else {
                            @Suppress("DEPRECATION")
                            activity?.windowManager?.defaultDisplay?.let {
                                fileWriterMag.append(it.rotation.toString())
                            }
                        }
                        fileWriterMag.append('\n')

                        println("Write CSV successfully!")

                    } catch (e: Exception) {
                        println("Writing CSV error!")
                        e.printStackTrace()
                    }
                }
                else -> {
                    //do nothing
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //do nothing
    }


    override fun onResume() {
        super.onResume()
        linearAcelSensor?.also { linearAc ->
            sensorManager.registerListener(this, linearAc, SensorManager.SENSOR_DELAY_NORMAL)
        }
        acelSensor?.also { acel ->
            sensorManager.registerListener(this, acel, SensorManager.SENSOR_DELAY_NORMAL)
        }
        gyrosSensor?.also { gyros ->
            sensorManager.registerListener(this, gyros, SensorManager.SENSOR_DELAY_NORMAL)
        }
        magSensor?.also { mag ->
            sensorManager.registerListener(this, mag, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onItemClick(iteration: Int) {
        binding.btCont.isEnabled = (iteration >= 30)
    }

    companion object {
        const val TYPE_LINEAR_ACCELERATION = 10
        const val TYPE_ACCELEROMETER = 1
        const val TYPE_GYROSCOPE = 4
        const val TYPE_MAGNETIC_FIELD = 2
    }
}