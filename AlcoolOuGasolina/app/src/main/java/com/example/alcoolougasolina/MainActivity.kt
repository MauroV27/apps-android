package com.example.alcoolougasolina

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var percentual:Double = 0.7
    var isSwitchActive : Boolean = false

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Code for read data in sharedPrefereces about percentual
        this.isSwitchActive = loadStatus()
        this.percentual = if ( this.isSwitchActive ) 0.75 else 0.7

        Log.d("PDM23","No onCreate, $percentual")

        val btCalc: Button = this.findViewById(R.id.btCalcular)
        val gasoline: EditText = this.findViewById(R.id.edGasolina)
        val alcool: EditText = this.findViewById(R.id.edAlcool)

        val resultMessage: TextView = this.findViewById(/* id = */ R.id.ResultMessage)
        val subMessage : TextView = this.findViewById(R.id.subText)
        val swPercent: Switch = this.findViewById(R.id.swPercentual)

        // Code for restore status in switch after onPause
        swPercent.isChecked = this.percentual == 0.75

        swPercent.setOnClickListener  {
            if ( swPercent.isChecked ) {
                this.percentual = 0.75
            } else {
                this.percentual = 0.7
            }
            this.isSwitchActive = swPercent.isChecked
            saveStatus(this.isSwitchActive)

            Log.d("PDM23", "value stored in pctg : ${this.percentual}")
        }

        btCalc.setOnClickListener(View.OnClickListener {
            resultMessage.text = ""
            subMessage.text = ""

            if ( gasoline.text.isEmpty() or alcool.text.isEmpty() ){
                subMessage.text = "Valores"
                resultMessage.text = "Inválidos"
                return@OnClickListener
            }

            val valueGasoline: Double = gasoline.text.toString().toDouble()
            val valueAlcool : Double = alcool.text.toString().toDouble()

            subMessage.text = "É recomendado:"

            if( valueAlcool <= this.percentual * valueGasoline ) {
                resultMessage.text = "Álcool!"
            } else {
                resultMessage.text = "Gasolina"
            }
        })

    }

    override fun onResume(){
        super.onResume()
        Log.d("PDM23","No onResume, $percentual")
    }

    override fun onStart(){
        super.onStart()
        Log.d("PDM23","No onStart")
    }

    override fun onPause(){
        super.onPause()
        Log.d("PDM23","No onPause")
    }

    override fun onStop(){
        super.onStop()
        Log.d("PDM23","No onStop")
    }

    override fun onDestroy(){
        super.onDestroy()
        Log.d("PDM23","No onDestroy")
    }

    private fun saveStatus(value : Boolean ){
        val sharerPreference : SharedPreferences = getSharedPreferences("SharedPercentage", Context.MODE_PRIVATE)
        val editData = sharerPreference.edit()

        editData.apply{
            putBoolean("active", value)
        }.apply()
    }

    private fun loadStatus() : Boolean {
        val sharerPreference : SharedPreferences = getSharedPreferences("SharedPercentage", Context.MODE_PRIVATE)

        return sharerPreference.getBoolean("active", false)
    }
}