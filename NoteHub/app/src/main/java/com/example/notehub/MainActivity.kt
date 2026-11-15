package com.example.notehub


import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notehub.data.NoteHubDatabaseInstance
import com.example.notehub.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
z
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Po kliknutí na FAB otevře AddNoteActivity
        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }


        /* Zobrazování poznámek */

        // 1) Vytvoření adaptéru
        val adapter = NoteAdapter()

        // 2) Nastavení RecyclerView
        binding.recyclerViewNotes.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewNotes.adapter = adapter

        // 3) Získání DAO
        val noteDao = NoteHubDatabaseInstance.getDatabase(applicationContext).noteDao()

        // 4) Pozorování dat pomocí Flow z Room
        // Spustí korutinu v rámci životního cyklu aktivity.
        // Když se aktivita zničí, korutina se automaticky ukončí.
        lifecycleScope.launch {
            // Sleduje Flow. Kdykoli se změní data v DB → seznam se okamžitě obnoví.
            noteDao.getAllNotes().collectLatest { notes ->
                //Pošle nová data do adapteru, aby se překreslil RecyclerView.
                adapter.submitList(notes)
            }
        }
    }
}