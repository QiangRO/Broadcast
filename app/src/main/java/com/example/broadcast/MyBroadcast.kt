package com.example.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.broadcast.databinding.ActivityBatteryBinding
import kotlin.math.round

class MyBroadcast(
    private val binding: ActivityBatteryBinding
): BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_BATTERY_CHANGED -> showBatteryLevel(intent)
            Intent.ACTION_BATTERY_LOW -> evaluateLowBattery(context, intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun evaluateLowBattery(context: Context?, intent: Intent?) {
        val lowBattery = intent?.getBooleanExtra(BatteryManager.EXTRA_BATTERY_LOW, false)
        lowBattery?.let {
            if (it) {
                binding.txtMensajeBateria.text = "Bateria normal"

            } else {
                binding.txtMensajeBateria.text = "Bateria baja"
                configureScreenBrightness(context)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.P)
    private fun configureScreenBrightness(context: Context?) {
        /*No se puede usar recursos de sistema si los permisos
        * no estan habilitados, las funciones no pueden ejecutar esos metodos*/
        val screenBrightnessLevel = 50
        if (hasWriteSettingsEnabled(context)){
            //1. El nivel del brillo se maneja de 0 a 255
            //2. Por defecto el brillo se ajusta automaticamente, por ende primero hay que cambiar a modo manual
            Settings.System.putInt(
                context?.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
            )
            Settings.System.putInt(
                context?.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS,
                screenBrightnessLevel
            )
            val percentaje = screenBrightnessLevel.toDouble() / 255
            Toast.makeText(context,"${round(percentaje * 100)}% ", Toast.LENGTH_SHORT).show()
        }else
        {
            Toast.makeText(context, "No puedes configurar los settings", Toast.LENGTH_SHORT ).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun showBatteryLevel(intent: Intent?) {
        val batteryLevel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
        batteryLevel.let{
            val porcentaje = "$it% bateria"
            binding.txtPorcentajeBateria.text = "$it%"
            binding.pbNivelBateria.progress= it!!
        }
        val health = intent?.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)
        health?.let {
            when (it) {
                BatteryManager.BATTERY_HEALTH_COLD -> binding.txtBatteryHealth.text = "Bateria fria"
                BatteryManager.BATTERY_HEALTH_DEAD -> binding.txtBatteryHealth.text =
                    "Bateria muerta"
                BatteryManager.BATTERY_HEALTH_GOOD -> binding.txtBatteryHealth.text =
                    "Bateria buena"
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> binding.txtBatteryHealth.text =
                    "Bateria sobrecargada"
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> binding.txtBatteryHealth.text =
                    "Bateria sobrecalentada"
                BatteryManager.BATTERY_HEALTH_UNKNOWN -> binding.txtBatteryHealth.text =
                    "Bateria desconocida"
                BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> binding.txtBatteryHealth.text =
                    "Bateria con falla no especificada"
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.P)
    private fun hasWriteSettingsEnabled(context: Context?):Boolean{
        return Settings.System.canWrite(context)
    }

    /*private fun evaluateBatteryHealth(intent: Intent?) {
        val health = intent?.getIntExtra(BatteryManager.EXTRA_HEALTH,0)
        health?.let {
            when (it) {
                BatteryManager.BATTERY_HEALTH_COLD -> {
                    binding.txtBatteryHealth.text = "Bateria fria"
                }
                BatteryManager.BATTERY_HEALTH_DEAD -> {
                    binding.txtBatteryHealth.text = "Bateria muerta"
                }
                BatteryManager.BATTERY_HEALTH_GOOD -> {
                    binding.txtBatteryHealth.text = "Bateria buena"
                }
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> {
                    binding.txtBatteryHealth.text = "Bateria sobrecargada"
                }
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> {
                    binding.txtBatteryHealth.text = "Bateria sobrecalentada"
                }
                BatteryManager.BATTERY_HEALTH_UNKNOWN -> {
                    binding.txtBatteryHealth.text = "Bateria desconocida"
                }
                BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> {
                    binding.txtBatteryHealth.text = "Bateria con falla no especificada"
                }
                else -> {
                    binding.txtBatteryHealth.text = "Bateria no compatible"
                }
            }
        }
    }*/

}