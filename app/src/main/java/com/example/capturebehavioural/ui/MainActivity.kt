package com.example.capturebehavioural.ui

import android.app.AlertDialog
import android.content.Intent
import android.provider.Settings
import com.example.capturebehavioural.R
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.*
import java.util.*
import android.os.*
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat

@ExperimentalCoroutinesApi
class MainActivity : FragmentActivity() {

    companion object {
        private const val APP_STORAGE_ACCESS_REQUEST_CODE = 501
        private const val DEBUG_TAG = "Gestures"
    }

    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDetector = GestureDetectorCompat(this, MyGestureListener())
        //requestPermissionManageAllFiles()
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        mDetector.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    private fun requestPermissionManageAllFiles() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R ||
            Environment.isExternalStorageManager()) {

        } else {
            val builder = AlertDialog.Builder(this@MainActivity)
                .setMessage(resources.getString(R.string.permissions))
                .setPositiveButton("OK") {
                        _, _ ->
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    startActivityForResult(intent, APP_STORAGE_ACCESS_REQUEST_CODE)
                }.setCancelable(false)

            val alert = builder.create()
            alert.setCancelable(false)
            alert.setCanceledOnTouchOutside(false)
            alert.show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != RESULT_OK && requestCode == APP_STORAGE_ACCESS_REQUEST_CODE) {
            requestPermissionManageAllFiles()
        }
    }


    private class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            Log.d(DEBUG_TAG, "onScroll: $e1 $e2")
            return true
        }

        override fun onFling(
            event1: MotionEvent,
            event2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            Log.d(DEBUG_TAG, "onFling: $event1 $event2")
            return true
        }
    }
}

