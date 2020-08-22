package jp.araobp.mpu9250

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import jp.araobp.mpu9250.analyzer.Oscilloscope
import jp.araobp.mpu9250.serial.Ak8963Data
import jp.araobp.mpu9250.serial.IDataReceiver
import jp.araobp.mpu9250.serial.Mpu9250Data
import jp.araobp.mpu9250.serial.Mpu9250Interface
import kotlinx.android.synthetic.main.activity_accel_gyro.*
import kotlinx.android.synthetic.main.activity_accel_gyro.buttonExit
import kotlinx.android.synthetic.main.activity_accel_gyro.surfaceViewAnalyzer
import kotlinx.android.synthetic.main.activity_accel_gyro.textViewDump
import kotlinx.android.synthetic.main.activity_accel_gyro.textViewDumpTitle
import kotlinx.android.synthetic.main.activity_accel_gyro.toggleButtonCapture
import kotlinx.android.synthetic.main.activity_accel_gyro.toggleButtonDump
import kotlinx.android.synthetic.main.activity_magneto.*


class AccelGyroActivity : AppCompatActivity() {

    companion object {
        val TAG: String = this::class.java.simpleName
        const val MAX_NUM_ENTRIES = 128
    }

    private var baudrate: Int = 115200
    private lateinit var mpu9250Interface: Mpu9250Interface

    private var oscilloscope: Oscilloscope? = null

    private fun setAccelRangeValue(spinnerItem: String) {
        mpu9250Interface.pause()

        when (spinnerItem) {
            "±2g" -> mpu9250Interface.setAccelRange(Mpu9250Interface.AccelRange.G_2)
            "±4g" -> mpu9250Interface.setAccelRange(Mpu9250Interface.AccelRange.G_4)
            "±8g" -> mpu9250Interface.setAccelRange(Mpu9250Interface.AccelRange.G_8)
            "±16g" -> mpu9250Interface.setAccelRange(Mpu9250Interface.AccelRange.G_16)
            else -> mpu9250Interface.setAccelRange(Mpu9250Interface.AccelRange.G_2)
        }

        mpu9250Interface.resume()
    }

    private fun setGyroRangeValue(spinnerItem: String) {
        mpu9250Interface.pause()

        when (spinnerItem) {
            "250dps" -> mpu9250Interface.setGyroRange(Mpu9250Interface.GyroRange.DPS_250)
            "500dps" -> mpu9250Interface.setGyroRange(Mpu9250Interface.GyroRange.DPS_500)
            "1000dps" -> mpu9250Interface.setGyroRange(Mpu9250Interface.GyroRange.DPS_1000)
            "2000dps" -> mpu9250Interface.setGyroRange(Mpu9250Interface.GyroRange.DPS_2000)
            else -> mpu9250Interface.setGyroRange(Mpu9250Interface.GyroRange.DPS_250)
        }

        mpu9250Interface.resume()
    }

        private fun updateWaveform(data: Any) {

        when(data) {
            is Mpu9250Data -> {
                oscilloscope?.update6axis(arrayOf(data.ax, data.ay, data.az, data.gx, data.gy, data.gz))
            }
            is Ak8963Data -> {

            }
        }
    }

    private fun enableDumpWindow(visible: Boolean) {
        if (visible) {
            textViewDumpTitle.visibility = View.VISIBLE
            textViewDump.visibility = View.VISIBLE
        }
        else {
            textViewDumpTitle.visibility = View.GONE
            textViewDump.visibility = View.GONE
        }
    }

    private fun fullscreen() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setContentView(R.layout.activity_accel_gyro)

        setTitle("Accelerometer and Gyroscope")

        fullscreen()

        textViewDumpTitle.movementMethod = ScrollingMovementMethod()

        baudrate = intent.getIntExtra(MainActivity.BAUDRATE, 115200)

        spinnerAccelRange.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) = Unit
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
                    setAccelRangeValue(spinnerAccelRange.selectedItem.toString())
                }
            }

        spinnerGyroRange.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) = Unit
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
                    setGyroRangeValue(spinnerGyroRange.selectedItem.toString())
                }
            }

        surfaceViewAnalyzer.post {
            oscilloscope = Oscilloscope(
                surfaceView = surfaceViewAnalyzer, maxNumEntries = MAX_NUM_ENTRIES)
        }

        toggleButtonDump.setOnCheckedChangeListener { _, isChecked ->
            enableDumpWindow(isChecked)
        }

        toggleButtonCapture.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mpu9250Interface.resume()
            } else {
                mpu9250Interface.pause()
            }
        }

        buttonExit.setOnClickListener {
            finish()
        }

        enableDumpWindow(toggleButtonDump.isChecked)
    }

    override fun onResume() {
        super.onResume()
        mpu9250Interface = Mpu9250Interface(this, baudrate,
            object : IDataReceiver {
                override fun onMpu9250Data(data: Mpu9250Data) {
                    if (toggleButtonCapture.isChecked) {
                        textViewDump.post {
                            textViewDump.append(data.toString() + "\n")
                        }
                        updateWaveform(data)
                    }
                }

                override fun onAk8963Data(data: Ak8963Data) {
                    // NOP
                }
            })
    }

    override fun onPause() {
        super.onPause()
        mpu9250Interface.destroy()
    }
}