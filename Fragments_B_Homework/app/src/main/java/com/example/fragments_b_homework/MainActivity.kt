package com.example.fragments_b_homework

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fragments_b_homework.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //proměnná se naplní později ne při vytvoření třídy
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // z activity_main.xml vytvoří objekt, přes který se dostaneme ke všem prvkům (tlačítka, kontejner)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FragmentA())
                .commit()
        }


        //nastavení fragmentu po kliknutí na první tlačítko

        binding.btnFragmentA.setOnClickListener {
            supportFragmentManager.beginTransaction()
                //vymění obsah kontejneru za Fragment A
                .replace(R.id.fragmentContainer, FragmentA())
                .commit()
        }
        binding.btnFragmentB.setOnClickListener {
            supportFragmentManager.beginTransaction()
                //vymění obsah kontejneru za Fragment B
                .replace(R.id.fragmentContainer, FragmentB())
                .commit()
        }
        binding.btnFragmentC.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FragmentC())
                .commit()
        }

    }
}
