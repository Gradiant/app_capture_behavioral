package com.example.capturebehavioural.ui.capture

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.GestureDetector
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.capturebehavioural.R
import java.io.File
import java.io.FileWriter

class CaptureActivity : FragmentActivity(), GestureDetector.OnGestureListener {

    private val touchFile: File = File.createTempFile("TouchEvent", "csv")
    private var fileWriterTouch: FileWriter = FileWriter(touchFile)

    private val scrollFile: File = File.createTempFile("ScrollEvent", "csv")
    private var fileWriterScroll: FileWriter = FileWriter(scrollFile)

    private val keyFile: File = File.createTempFile("KeyEvent", "csv")
    var fileWriterKey: FileWriter = FileWriter(keyFile)

    var text = ""

    private lateinit var viewModel: CaptureViewModel

    private lateinit var mDetector: GestureDetectorCompat

    private lateinit var email: String
    private lateinit var season: String
    private lateinit var position: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)


        viewModel = ViewModelProvider(this,
            CaptureViewModel.MainViewModelFactory()
        )[CaptureViewModel::class.java]

        email = this.intent?.getStringExtra("email") ?: ""
        season = this.intent?.getStringExtra("season") ?: ""
        position = this.intent?.getStringExtra("position") ?: "0"

        mDetector = GestureDetectorCompat(this, this)
    }


    override fun onPause() {
        fileWriterScroll.close()
        fileWriterTouch.close()
        fileWriterKey.close()
        viewModel.saveValues(email, season, "ScrollEvent", scrollFile)
        viewModel.saveValues(email, season, "TouchEvent", touchFile)
        viewModel.saveValues(email, season, "KeyEvent", keyFile)

        super.onPause()
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        mDetector.onTouchEvent(event)
        if (currentFocus != null) {
            val imm = this!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this!!.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(event) // so that viewPager and other views also get the event
    }

    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            try {
                fileWriterKey.append(System.currentTimeMillis().toString())
                fileWriterKey.append(',')
                fileWriterKey.append(event.eventTime.toString())
                fileWriterKey.append(',')
                fileWriterKey.append("0")
                fileWriterKey.append(',')
                fileWriterKey.append(event.keyCode.toString())
                fileWriterKey.append(',')
                fileWriterKey.append(position)
                fileWriterKey.append(',')

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    this@CaptureActivity.display?.let {
                        fileWriterKey.append(it.rotation.toString())
                    }
                } else {
                    @Suppress("DEPRECATION")
                    this@CaptureActivity.windowManager?.defaultDisplay?.let {
                        fileWriterKey.append(it.rotation.toString())
                    }
                }
                fileWriterKey.append('\n')

                println("Write CSV successfully!")
            } catch (e: Exception) {
                println("Writing CSV error!")
                e.printStackTrace()
            }
        } else if (event.action == KeyEvent.ACTION_UP) {
            try {
                fileWriterKey.append(System.currentTimeMillis().toString())
                fileWriterKey.append(',')
                fileWriterKey.append(event.eventTime.toString())
                fileWriterKey.append(',')
                fileWriterKey.append("1")
                fileWriterKey.append(',')
                fileWriterKey.append(event.keyCode.toString())
                fileWriterKey.append(',')
                fileWriterKey.append(position)
                fileWriterKey.append(',')

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    this@CaptureActivity.display?.let {
                        fileWriterKey.append(it.rotation.toString())
                    }
                } else {
                    @Suppress("DEPRECATION")
                    this@CaptureActivity.windowManager?.defaultDisplay?.let {
                        fileWriterKey.append(it.rotation.toString())
                    }
                }
                fileWriterKey.append('\n')

                println("Write CSV successfully!")
            } catch (e: Exception) {
                println("Writing CSV error!")
                e.printStackTrace()
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onDown(event: MotionEvent?): Boolean {
        try {
            event?.let { e1 ->
                fileWriterTouch.append(System.currentTimeMillis().toString())
                fileWriterTouch.append(',')
                fileWriterTouch.append(e1.eventTime.toString())
                fileWriterTouch.append(',')
                fileWriterTouch.append(e1.pointerCount.toString())
               /* fileWriterScroll.append(',')
                fileWriterScroll.append(e1.getPointerId().toString())*/
                fileWriterTouch.append(',')
                fileWriterTouch.append(e1.action.toString())
                fileWriterTouch.append(',')
                fileWriterTouch.append(e1.x.toString())
                fileWriterTouch.append(',')
                fileWriterTouch.append(e1.y.toString())
                fileWriterTouch.append(',')
                fileWriterTouch.append(e1.pressure.toString())
                fileWriterTouch.append(',')
                fileWriterTouch.append(e1.size.toString())
                fileWriterTouch.append(',')
                fileWriterTouch.append(position)
                fileWriterTouch.append(',')

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    this@CaptureActivity.display?.let {
                        fileWriterTouch.append(it.rotation.toString())
                    }
                } else {
                    @Suppress("DEPRECATION")
                    this@CaptureActivity.windowManager?.defaultDisplay?.let {
                        fileWriterTouch.append(it.rotation.toString())
                    }
                }
                fileWriterTouch.append('\n')

                println("Write CSV successfully!")
            }
        } catch (e: Exception) {
            println("Writing CSV error!")
            e.printStackTrace()
        }
        return true
    }

    override fun onShowPress(p0: MotionEvent?) {
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return true
    }

    override fun onScroll(event1: MotionEvent?, event2: MotionEvent?, p2: Float, p3: Float): Boolean {
        try {
            event1?.let { e1 ->
                event2?.let { e2 ->
                    fileWriterScroll.append(System.currentTimeMillis().toString())
                    fileWriterScroll.append(',')
                    fileWriterScroll.append(e1.eventTime.toString())
                    fileWriterScroll.append(',')
                    fileWriterScroll.append(e2.eventTime.toString())
                    fileWriterScroll.append(',')
                    fileWriterScroll.append("0")
                    fileWriterScroll.append(',')
                    fileWriterScroll.append(e1.x.toString())
                    fileWriterScroll.append(',')
                    fileWriterScroll.append(e1.y.toString())
                    fileWriterScroll.append(',')
                    fileWriterScroll.append(e1.pressure.toString())
                    fileWriterScroll.append(',')
                    fileWriterScroll.append(e1.size.toString())
                    fileWriterScroll.append(',')
                    fileWriterScroll.append("2")
                    fileWriterScroll.append(',')
                    fileWriterScroll.append(e2.x.toString())
                    fileWriterScroll.append(',')
                    fileWriterScroll.append(e2.y.toString())
                    fileWriterScroll.append(',')
                    fileWriterScroll.append(e2.pressure.toString())
                    fileWriterScroll.append(',')
                    fileWriterScroll.append(e2.size.toString())
                    fileWriterScroll.append(',')
                    fileWriterScroll.append(position)
                    fileWriterScroll.append(',')

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        this@CaptureActivity.display?.let {
                            fileWriterScroll.append(it.rotation.toString())
                        }
                    } else {
                        @Suppress("DEPRECATION")
                        this@CaptureActivity.windowManager?.defaultDisplay?.let {
                            fileWriterScroll.append(it.rotation.toString())
                        }
                    }
                    fileWriterScroll.append('\n')

                    println("Write CSV successfully!")
                }
            }
        } catch (e: Exception) {
            println("Writing CSV error!")
            e.printStackTrace()
        }
        return true
    }

    override fun onLongPress(p0: MotionEvent?) {
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return true
    }

}

