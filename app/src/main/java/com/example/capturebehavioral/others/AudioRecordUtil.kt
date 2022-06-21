package com.example.capturebehavioral.others

import android.app.Application
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Environment
import java.io.*

class AudioRecordUtil(private val application: Application) {

    private val sampleRateInHz = 16000
    private val channelConfig: Int = AudioFormat.CHANNEL_IN_MONO
    // Formato de datos de audio: PCM 16 bits por muestra
    private val audioFormat: Int = AudioFormat.ENCODING_PCM_16BIT

    private var recorder: AudioRecord? = null
    private var recordMinBufferSize = 0
    private var recordingThread: Thread? = null
    private var isRecording = false

    init {
        recordMinBufferSize =
            AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat)

        if (checkRecordingPermission()) {
            recorder = AudioRecord(MediaRecorder.AudioSource.MIC, sampleRateInHz, channelConfig,
                audioFormat, recordMinBufferSize
            )
        }
    }

    private fun checkRecordingPermission(): Boolean {
        val permission = "android.permission.RECORD_AUDIO"
        val result: Int = application.checkCallingOrSelfPermission(permission)
        return result == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Empezar a grabar
     */
    fun startRecording(b: Boolean) {
        recorder!!.startRecording()
        isRecording = true
        recordingThread = Thread({ writeAudioDataToFile(b) }, "AudioRecorder Thread")
        recordingThread!!.start()
    }

    private fun getTempFilename(): String {
        val filepath = Environment.getExternalStorageDirectory().path
        val fileName = "recording_temp.raw"
        val file = File(filepath, fileName)

        try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }


    fun stopRecording(b: Boolean, recordingName: String) {
        if (recorder != null) {
            isRecording = false
            recorder!!.stop()
            recorder!!.release()
            recorder = null
            recordingThread = null
        }
        if (b) {
            getTempFilename().let { getFilename(recordingName)?.let { it1 -> copyWaveFile(it, it1) } }
            deleteTempFile()
        }
    }


    fun getFilename(recordingName: String): String? {
        val filepath = Environment.getExternalStorageDirectory().path
        val file = File("$filepath/$recordingName.wav")
        try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    private fun writeAudioDataToFile(b: Boolean) {
        val data = ByteArray(recordMinBufferSize)
        val filename = getTempFilename()
        var os: FileOutputStream? = null
        try {
            os = FileOutputStream(filename, b)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        if (os != null) {
            while (isRecording) {
                val read = recorder!!.read(data, 0, recordMinBufferSize)
                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                    try {
                        os.write(data)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            try {
                os.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun deleteTempFile() {
        val file = File(getTempFilename())
        file.delete()
    }

    private fun copyWaveFile(inFilename: String, outFilename: String) {
        var `in`: FileInputStream? = null
        var out: FileOutputStream? = null
        val channels = 1
        val data = ByteArray(recordMinBufferSize)

        try {
            `in` = FileInputStream(inFilename)
            out = FileOutputStream(outFilename, false)
            val totalAudioLen = `in`.channel.size()
            val totalDataLen = totalAudioLen + 44

            writeWaveFileHeader(
                out, totalAudioLen, totalDataLen, channels, outFilename
            )
            while (`in`.read(data) !== -1) {
                out.write(data)
            }
            `in`.close()
            out.close()


        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun writeWaveFileHeader(
        out: FileOutputStream, totalAudioLen: Long,
        totalDataLen: Long, channels: Int, outFileName: String
    ) {
        val byteRate = (16000 * channels * 16) / 8
        val header = ByteArray(44)

        header[0] = 'R'.code.toByte()
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()

        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = (totalDataLen shr 8 and 0xff).toByte()
        header[6] = (totalDataLen shr 16 and 0xff).toByte()
        header[7] = (totalDataLen shr 24 and 0xff).toByte()

        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()

        header[12] = 'f'.code.toByte() // 'fmt ' chunk
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()

        header[16] = 16 // 4 bytes: size of 'fmt ' chunk
        header[17] = 0
        header[18] = 0
        header[19] = 0

        header[20] = 1 // format = 1
        header[21] = 0

        header[22] = channels.toByte()
        header[23] = 0

        header[24] = (16000 and 0xff).toByte()
        header[25] = (16000 shr 8 and 0xff).toByte()
        header[26] = (16000 shr 16 and 0xff).toByte()
        header[27] = (16000 shr 24 and 0xff).toByte()

        header[28] = (byteRate and 0xff).toByte()
        header[29] = (byteRate shr 8 and 0xff).toByte()
        header[30] = (byteRate shr 16 and 0xff).toByte()
        header[31] = (byteRate shr 24 and 0xff).toByte()

        header[32] = (channels * 16 / 8).toByte() // block align
        header[33] = 0

        header[34] = 16 // bits per sample
        header[35] = 0

        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()

        header[40] = (totalAudioLen and 0xff).toByte()
        header[41] = (totalAudioLen shr 8 and 0xff).toByte()
        header[42] = (totalAudioLen shr 16 and 0xff).toByte()
        header[43] = (totalAudioLen shr 24 and 0xff).toByte()

        out.write(header, 0, 44)
    }


}