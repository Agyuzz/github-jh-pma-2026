package com.example.objednavkova_aplikace

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.objednavkova_aplikace.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        //enableEdgeToEdge()
        setContentView(binding.root)

        // Ošetření systémových lišt – použije se přímo binding.main nebo binding.root
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //volá se po startu, aby se zobrazila výchozí cena verze hry
        updatePrice()

    }

    private fun updatePrice(){
        var totalPrice = 0;

        //1. zjištění základní ceny vybraného kola
        totalPrice += when(binding.rgGameVersion.checkedRadioButtonId){
            binding.rbWotlk.id -> 600
            binding.rbDragonflight.id -> 800
            binding.rbForever.id -> 1000
            else -> 0
        }
        //2. přičtení příplatkové výbavy

        if (binding.cbGameTime.isChecked) totalPrice += 300
        if (binding.cbBoost.isChecked) totalPrice += 200
        if (binding.cbMount.isChecked) totalPrice += 600

        //3. zobrazení výsledků v barevném textview
        binding.tvTotalPrice.text = "Celkem: $totalPrice Kč"

    }

}