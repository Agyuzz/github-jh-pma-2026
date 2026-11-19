package com.example.notehub

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notehub.data.Note
import com.example.notehub.data.NoteHubDatabaseInstance
import com.example.notehub.databinding.ActivityAddNoteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = NoteHubDatabaseInstance.getDatabase(this)
        val noteDao = db.noteDao()

        // ---------- Datum vytvoření ----------
        // aktuální čas v milisekundách; uložíme ho i do poznámky
        val now = System.currentTimeMillis()
        // převedeme milisekundy na čitelný text, např. 19.11.2026 14:05
        val formatted = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            .format(Date(now))
        binding.textCreatedAtInfo.text = "Vytvořeno: $formatted"

        // ---------- Uložení poznámky ----------
        binding.btnSaveNote.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val content = binding.editTextContent.text.toString()

            // pozice vybrané položky ve Spinneru (0, 1 nebo 2) = hodnota priority
            val priority = binding.spinnerPriority.selectedItemPosition

            // text vybrané kategorie ze Spinneru (např. "Škola")
            val category = binding.spinnerCategory.selectedItem.toString()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Vyplň název i obsah", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val note = Note(
                title = title,
                content = content,
                priority = priority,
                category = category,
                createdAt = now // ZMĚNA: stejný čas, který vidí uživatel v popisku
            )

            // Spustí korutinu na vlákně určeném pro vstupně/výstupní operace (IO),
            // které jsou vhodné pro práci s databází, aby se nezablokovalo UI.
            CoroutineScope(Dispatchers.IO).launch {
                // Uložení poznámky do databáze na vlákně pro IO operace (nezablokuje UI)
                noteDao.insert(note)

                // Po uložení zavře tuto aktivitu a vrátí se zpět na hlavní obrazovku
                finish()
            }
        }

    }
}