package com.example.objednavkova_aplikace

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.objednavkova_aplikace.databinding.ActivitySummaryBinding

class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()

        // Inicializace ViewBindingu
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ochrana před překryvem systémovými lištami
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Přidá vnitřní okraj (padding) odpovídající velikosti horní a spodní systémové lišty
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //příjmutí dat z intentu
        val gameVersion = intent.getStringExtra("EXTRA_GAME_VERSION") ?: "neznámá verze"
        val totalPrice = intent.getStringExtra("EXTRA_TOTAL_PRICE") ?: "Celkem 0 Kč"

        val hasGameTime = intent.getBooleanExtra("EXTRA_HAS_GAMETIME", false)
        val hasBoost = intent.getBooleanExtra("EXTRA_HAS_BOOST", false)
        val hasMount = intent.getBooleanExtra("EXTRA_HAS_MOUNT", false)

        //sestavení textu pro příplatkové bonusíky
        var extrasText = "Příplatkové bonusy: \n"
        var hasAnyExtra = false

        if (hasGameTime){
            extrasText += "Gametime na víc \n"
            hasAnyExtra = true
        }

        if (hasBoost){
            extrasText += "Rychlý catch-up \n"
            hasAnyExtra = true
        }

        if (hasMount){
            extrasText += "Ušlechtilý mount paladina \n"
            hasAnyExtra = true
        }

        //pokud uživatel nezaškrtnul nic
        if (!hasAnyExtra){
            extrasText += "- žádná"
        }

        //zobrazení dat v textovém poli
        binding.tvSelectedBike.text = "Vybraná verze hry: $gameVersion"
        binding.tvExtras.text = extrasText
        binding.tvFinalPrice.text = totalPrice

        //Obsluha tlačítka Zpět pro návrat na úpravu objednávky
        binding.btnBack.setOnClickListener {
            finish()
        }




    }
}