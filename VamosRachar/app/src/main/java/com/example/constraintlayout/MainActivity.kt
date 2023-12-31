package com.example.constraintlayout

import android.annotation.SuppressLint
import android.content.Intent
//import android.icu.number.NumberFormatter
import android.icu.text.DecimalFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
//import android.widget.Button
import android.widget.ImageButton
import java.util.*

class MainActivity : AppCompatActivity() , TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtConta : EditText
    private lateinit var edtNumPeople : EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.edtConta = findViewById<EditText>(R.id.edtConta)
        this.edtNumPeople = findViewById<EditText>(R.id.edtNumPeople)

        this.edtConta.addTextChangedListener(this)
        this.edtNumPeople.addTextChangedListener(this)

        val textResult = findViewById<TextView>(R.id.txtResult)
        textResult.text = getString(R.string.cd_res_default_value)

        // Initialize TTS engine
        tts = TextToSpeech(this, this)

        val btShare : ImageButton = findViewById<ImageButton>(R.id.btShare)
        btShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.cd_account_resp_msg,textResult.text.toString()))
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, getString(R.string.INTENT_share_count))
            startActivity(shareIntent)
        }

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
       Log.d("PDM23","Antes de mudar")
        Log.d ("PDM23", s.toString())
        val value = "${this.edtConta.text} | ${this.edtNumPeople.text}"
        Log.d ("PDM23", value)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("PDM23","Mudando")
        Log.d ("PDM23", s.toString())
        val value = "${this.edtConta.text} | ${this.edtNumPeople.text}"
        Log.d ("PDM23", value)
    }

    override fun afterTextChanged(s: Editable?) {
        Log.d ("PDM23", "Depois de mudar")
        Log.d ("PDM23", s.toString())

        if ( this.edtConta.text.isEmpty() || this.edtNumPeople.text.isEmpty() ) return
        if ( this.edtConta.text.toString() == "." ) return // fixed bug when text is just "."

        val valueConta: Double = this.edtConta.text.toString().toDouble()
        val numPeople = this.edtNumPeople.text.toString().toInt()

        if ( valueConta < 0 || numPeople < 1 ) return

        val resultValue : Double = valueConta / numPeople
        val value = DecimalFormat("#.00").format(resultValue)
        Log.d ("PDM23", value)

        val textResult = findViewById<TextView>(R.id.txtResult)
        textResult.text = getString(R.string.cd_res_message_value, value)
    }

    fun clickFalar(v: View){
        val textResult = findViewById<TextView>(R.id.txtResult)
        var textToSpech : String

        if ( textResult.text.toString() == getString(R.string.cd_res_default_value) ){
            textToSpech = getString(R.string.ACTION_text_to_spech_invalid)
        } else {
            textToSpech = getString(R.string.cd_account_resp_msg,textResult.text.toString())
        }
        tts.speak(textToSpech, TextToSpeech.QUEUE_FLUSH, null, null)
    }
    override fun onDestroy() {
            // Release TTS engine resources
            tts.stop()
            tts.shutdown()
            super.onDestroy()
        }

    override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                // TTS engine is initialized successfully
                tts.language = Locale.getDefault()
                Log.d("PDM23","Sucesso na Inicialização")
            } else {
                // TTS engine failed to initialize
                Log.e("PDM23", "Failed to initialize TTS engine.")
            }
        }

}


