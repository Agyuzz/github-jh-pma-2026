package com.example.objednavkova_aplikace

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.objednavkova_aplikace.databinding.ActivityMainBinding
import kotlin.jvm.java

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

        //nastaveni listeneru na změny pro RadioGroup
        binding.rgGameVersion.setOnCheckedChangeListener { _, _ -> updatePrice() }

        //nastavení listeneru na změny pro checkboxy
        binding.cbMount.setOnClickListener { updatePrice() }
        binding.cbBoost.setOnClickListener { updatePrice() }
        binding.cbGameTime.setOnClickListener { updatePrice() }

        // změna obrázku podle vybrané verze
        binding.rbWotlk.setOnClickListener {
            binding.ivGameVersionPic.setImageResource(R.drawable.wotlk_copy)
        }
        binding.rbDragonflight.setOnClickListener {
            binding.ivGameVersionPic.setImageResource(R.drawable.dragonflight_copy)
        }
        binding.rbForever.setOnClickListener {
            binding.ivGameVersionPic.setImageResource(R.drawable.forever_copy)
        }

        binding.btnOrder.setOnClickListener {
            val gameVersion = when (binding.rgGameVersion.checkedRadioButtonId){
                binding.rbWotlk.id -> binding.rbWotlk.text.toString()
                binding.rbDragonflight.id -> binding.rbDragonflight.text.toString()
                binding.rbForever.id -> binding.rbForever.text.toString()
                else -> "Neznámá verze"
            }
            //extrakce výsledné ceny (vezmeme ji rovnou z textview)
            val currentPriceText = binding.tvTotalPrice.text.toString()

            //vytvoření intentu směřujícího na novou aktivitu (SummaryActivity)
            val intent = Intent(this, SummaryActivity::class.java)

            //přibalení dat jako klíč-hodnot
            intent.putExtra("EXTRA_GAME_VERSION", gameVersion)
            intent.putExtra("EXTRA_TOTAL_PRICE", currentPriceText)

            //přidání informace o nadstandardní výbavě (boolean hodnoty)
            intent.putExtra("EXTRA_HAS_GAMETIME", binding.cbGameTime.isChecked)
            intent.putExtra("EXTRA_HAS_BOOST", binding.cbBoost.isChecked)
            intent.putExtra("EXTRA_HAS_MOUNT", binding.cbMount.isChecked)

            //Spuštění druhé aktivity
            startActivity(intent)
        }


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