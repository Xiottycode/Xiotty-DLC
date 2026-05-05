package com.xiotty.dlc

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import rikka.shizuku.Shizuku

class MainActivity : AppCompatActivity() {
    private lateinit var txtStatus: TextView
    private var shizukuAvailable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtStatus = findViewById(R.id.txtStatus)
        val btnInject = findViewById<Button>(R.id.btnInject)

        findViewById<Switch>(R.id.switchBoxESP).setOnCheckedChangeListener { _, isChecked ->
            NativeBridge.setESPBox(isChecked)
        }
        findViewById<Switch>(R.id.switchSnaplines).setOnCheckedChangeListener { _, isChecked ->
            NativeBridge.setSnaplines(isChecked)
        }
        findViewById<Switch>(R.id.switchSilentAim).setOnCheckedChangeListener { _, isChecked ->
            NativeBridge.setSilentAim(isChecked)
        }
        val seekFOV = findViewById<SeekBar>(R.id.seekFOV)
        val txtFOV = findViewById<TextView>(R.id.txtFOVValue)
        seekFOV.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, value: Int, from: Boolean) {
                txtFOV.text = value.toString()
                NativeBridge.setAimFOV(value)
            }
            override fun onStartTrackingTouch(seek: SeekBar) {}
            override fun onStopTrackingTouch(seek: SeekBar) {}
        })
        findViewById<Switch>(R.id.switchChams).setOnCheckedChangeListener { _, isChecked ->
            NativeBridge.setChams(isChecked)
        }
        findViewById<Switch>(R.id.switchBunnyHop).setOnCheckedChangeListener { _, isChecked ->
            NativeBridge.setBunnyHop(isChecked)
        }

        checkShizuku()

        btnInject.setOnClickListener {
            if (shizukuAvailable) {
                val pid = getPidByName("com.standleo.private")
                if (pid != -1) {
                    NativeBridge.injectLibrary(pid, "/data/local/tmp/libxiotty.so")
                    txtStatus.text = "Status: Injected ✓"
                    txtStatus.setTextColor(0xFF55FF55.toInt())
                    Toast.makeText(this, "Xiotty DLC Injected", Toast.LENGTH_SHORT).show()
                } else {
                    txtStatus.text = "Status: Standleo not running"
                }
            } else {
                txtStatus.text = "Status: Shizuku not available"
            }
        }
    }

    private fun checkShizuku() {
        if (Shizuku.pingBinder()) {
            shizukuAvailable = true
            txtStatus.text = "Status: Shizuku OK, ready"
            txtStatus.setTextColor(0xFF55FF55.toInt())
        } else {
            shizukuAvailable = false
            txtStatus.text = "Status: Shizuku not running"
        }
    }

    private fun getPidByName(processName: String): Int {
        return try {
            java.lang.Runtime.getRuntime().exec(arrayOf("pidof", processName))
                .inputStream.bufferedReader().readText().trim().toIntOrNull() ?: -1
        } catch (e: Exception) { -1 }
    }

    companion object {
        init {
            System.loadLibrary("xiotty")
        }
    }
}
