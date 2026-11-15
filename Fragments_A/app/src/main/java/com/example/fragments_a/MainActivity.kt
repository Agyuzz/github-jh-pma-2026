package com.example.fragments_a

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fragments_a.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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


    }
}
