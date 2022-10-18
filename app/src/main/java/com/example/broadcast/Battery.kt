package com.example.broadcast

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.example.broadcast.databinding.ActivityBatteryBinding

class Battery : AppCompatActivity() {
    private lateinit var binding: ActivityBatteryBinding
    private lateinit var myBroadcast: MyBroadcast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBatteryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myBroadcast = MyBroadcast(binding)
        binding.floatingActionButton.setOnClickListener {
            enabledWriteSettings()

        }
    }

    //Metodo para habilitar el permiso de escritura
    private fun enabledWriteSettings() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(myBroadcast, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        registerReceiver(myBroadcast, IntentFilter(Intent.ACTION_BATTERY_LOW))
        registerReceiver(myBroadcast, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val intent = Intent(this, MyBroadcast::class.java)
        sendBroadcast(intent)
    }
    override fun onStop() {
        super.onStop()
        unregisterReceiver(myBroadcast)
    }
}