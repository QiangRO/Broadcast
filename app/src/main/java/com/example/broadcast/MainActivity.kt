package com.example.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import com.example.broadcast.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var voz:TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        voz = TextToSpeech(this, this)
    }

    override fun onInit(status: Int) {
        var respuesta = if (status == TextToSpeech.SUCCESS){
            voz.language = Locale("en-GB")
            "Todo ha salido bien"
        }else "Algo ha fallado, prueba mas tarde"
        Toast.makeText(this,respuesta, Toast.LENGTH_SHORT).show()
    }

    //Variable para configurar un broadcastReceiver
    private val obtenerModoAvion = object: BroadcastReceiver(){

        //Es fundamental sobreescribir el metodo llamado onReceive
        //Porque da la posibilidad de recibir informacion del evento de sistema
        //Y definimos la logica que se desea aplicar a partir de ese evento e informacion
        override fun onReceive(context: Context?, intent: Intent?) {
            val modoAvion = intent?.getBooleanExtra("state", false)
            modoAvion?.let {

                if (it){
                    binding.txtModoAvion.text = "Modo avion activado"
                    voz.speak("Modo avion activado", TextToSpeech.QUEUE_FLUSH, null, "")
                }
                else{
                    binding.txtModoAvion.text = "Modo avion desactivado"
                    voz.speak("Modo avion desactivado", TextToSpeech.QUEUE_FLUSH, null, "")
                }
            }
        }
    }
    //Broadcast para tratar los cambios de tiempo
    //Time Tick
    private val obtenerCambioTiempo = object:BroadcastReceiver(){
        var n = 0
        override fun onReceive(context: Context?, intent: Intent?) {
            for (i in 0..1){
                n++
                binding.txtTimeTick.text = "El tiempo ha cambiado $n veces"
                voz.speak("El tiempo ha cambiado $n veces", TextToSpeech.QUEUE_FLUSH, null, "")
            }
        }
    }
    //Configurar un broadcast que nos permitira comunicarse con el servicio de wifi
    private val obtenerWifi = object:BroadcastReceiver(){
        //El valor por defecto tiene que referir a que no puede resolver el servicio
        override fun onReceive(context: Context?, intent: Intent?) {
            val wifiMode = intent?.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
            wifiMode?.let {
                when(it){
                    WifiManager.WIFI_STATE_ENABLED -> {
                        binding.txtWifi.text = "Wifi activado"
                        voz.speak("Wifi activado", TextToSpeech.QUEUE_FLUSH, null, "")
                    }
                    WifiManager.WIFI_STATE_DISABLED -> {
                        binding.txtWifi.text = "Wifi desactivado"
                        voz.speak("Wifi desactivado", TextToSpeech.QUEUE_FLUSH, null, "")
                    }
                    WifiManager.WIFI_STATE_UNKNOWN -> {
                        binding.txtWifi.text = "Wifi desconocido"
                        voz.speak("Wifi desconocido", TextToSpeech.QUEUE_FLUSH, null, "")
                    }
                    else -> {
                        binding.txtWifi.text = "Dispositivo no compatible"
                        voz.speak("Dispositivo no compatible", TextToSpeech.QUEUE_FLUSH, null, "")
                    }
                }
            }
        }
    }
    //Time Tick
    override fun onStart() {
        super.onStart()
        //Registrar el broadcastReceiver
        registerReceiver(obtenerModoAvion, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
        registerReceiver(obtenerCambioTiempo, IntentFilter(Intent.ACTION_TIME_TICK))
        registerReceiver(obtenerWifi, IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(obtenerModoAvion)
        unregisterReceiver(obtenerCambioTiempo)
        unregisterReceiver(obtenerWifi)
    }

}