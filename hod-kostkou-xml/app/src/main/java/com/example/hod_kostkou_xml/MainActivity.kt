package com.example.hod_kostkou_xml

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    private var hits = 0
    private var rolls = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.llMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val diceSymbols = listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")
        val tvDice = findViewById<TextView>(R.id.tvDice)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val tvScore = findViewById<TextView>(R.id.tvScore)
        val btnEven = findViewById<Button>(R.id.btEven)
        val btnOdd = findViewById<Button>(R.id.btOdd)

        fun play(guessEven: Boolean){
            lifecycleScope.launch {
                btnEven.isEnabled = false
                btnOdd.isEnabled = false
                tvResult.text = "Kostka se točí..."

                repeat(times = 10){
                    tvDice.text = diceSymbols.random()
                    delay(timeMillis = 250)
                }

                val diceValue = (1..6).random()
                tvDice.text = diceSymbols[diceValue - 1]

                val isEven = diceValue % 2 == 0
                rolls++
                if (isEven == guessEven){
                    hits++
                    tvResult.text = "Padlo $diceValue - trefa"
                } else {
                    tvResult.text = "Padlo $diceValue - skoro :/"
                }
                tvScore.text = "Skóre: $hits / $rolls"

                btnEven.isEnabled = true
                btnOdd.isEnabled = true

            }
        }

        btnEven.setOnClickListener { play(guessEven = true) }
        btnOdd.setOnClickListener { play(guessEven = false) }

    }
}