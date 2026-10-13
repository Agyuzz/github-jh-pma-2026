package com.example.objednavka

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.objednavka.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //1. Binding - inicializace/deklarace binding objektu s odloženou inicializací
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()

        //2. binding - nafouknutí (inflate) layoutu do binding instance
        binding = ActivityMainBinding.inflate(layoutInflater)

        //3. Nastavení kořenového pohledu (root) do okna aktivity
        setContentView(binding.root)

        //setContentView(R.layout.activity_main)
        // Ošetření systémových lišt – použije se přímo binding.main nebo binding.root
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rbWotlk.setOnClickListener {
            binding.ivGameVersionPick.setImageResource(R.drawable.wotlk_copy)
        }

        binding.rbForever.setOnClickListener {
            binding.ivGameVersionPick.setImageResource(R.drawable.forever_copy)
        }

        binding.rbDragonFlight.setOnClickListener {
            binding.ivGameVersionPick.setImageResource(R.drawable.dragonflight_copy)
        }

        binding.btnOrder.setOnClickListener {
            val verzeHry = when(binding.rgGameVersion.checkedRadioButtonId){
                binding.rbWotlk.id -> binding.rbWotlk //Pokud sedí ID prvního tlačítka, použij rbWotlk
                binding.rbForever.id -> binding.rbForever
                binding.rbDragonFlight.id -> binding.rbDragonFlight

                else -> binding.rbWotlk //záložní možnost (fallback), kyby nebylo vybráno nic
            }
            val gametime = binding.cbGametime.isChecked
            val mount = binding.cbMount.isChecked
            val boost = binding.cbBoost.isChecked


            val orderText = getString(R.string.order_summary, verzeHry.text) +
                    (if (gametime) " " + getString(R.string.extra_gametime) else "") +
                    (if (mount) " " + getString(R.string.extra_mount) else "") +
                    (if (boost) " " + getString(R.string.extra_boost) else "")

            binding.tvOrder.text = orderText

            //změna obrázku v závislosti na vybraném radiobuttonu

            Toast.makeText(
                this,
                getString(R.string.order_toast, verzeHry.text),
                Toast.LENGTH_SHORT
            ) .show()

        }

    }
}