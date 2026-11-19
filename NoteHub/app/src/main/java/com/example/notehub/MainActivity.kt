package com.example.notehub

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notehub.data.Note
import com.example.notehub.data.NoteDao
import com.example.notehub.data.NoteHubDatabaseInstance
import com.example.notehub.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteDao: NoteDao
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // drží aktuálně hledaný text; prázdný text = zobrazit všechny poznámky
        val searchQuery = MutableStateFlow("")

        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        // DAO
        noteDao = NoteHubDatabaseInstance.getDatabase(applicationContext).noteDao()

        // Adapter s callbacky
        adapter = NoteAdapter(
            onEditClick = { note ->
                val intent = Intent(this, EditNoteActivity::class.java)
                intent.putExtra("note_id", note.id)
                startActivity(intent)
            },
            onDeleteClick = { note ->
                // nejdřív se zeptáme uživatele (úkol 15.1)
                showDeleteConfirmationDialog(note)
            }
        )

        // RecyclerView
        binding.recyclerViewNotes.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewNotes.adapter = adapter

        // --- Vyhledávání v poznámkách ---
        // Listener SearchView: při každé změně textu zapíše novou hodnotu do searchQuery
        binding.searchViewNotes.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            // potvrzení klávesnicí nepotřebujeme, hledá se už při psaní
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery.value = newText ?: ""
                return true
            }
        })

        // Jeden společný collector, který reaguje na změnu hledaného textu.
        // Nahrazuje původní samostatný blok "
        // Flow" s getAllNotes().
        lifecycleScope.launch {
            searchQuery.collectLatest { text ->
                if (text.isEmpty()) {
                    // prázdné pole -> zobrazit všechny poznámky
                    noteDao.getAllNotes().collectLatest { notes ->
                        adapter.submitList(notes)
                    }
                } else {
                    // něco je zadané -> zobrazit jen odpovídající poznámky
                    noteDao.searchNotes(text).collectLatest { notes ->
                        adapter.submitList(notes)
                    }
                }
            }
        }
    }

    // zobrazí potvrzovací dialog (běží na hlavním vlákně, protože jde o UI)
    private fun showDeleteConfirmationDialog(note: Note) {
        AlertDialog.Builder(this)
            .setMessage("Opravdu chcete smazat tuto poznámku?")
            .setPositiveButton("ANO") { _, _ ->
                // ANO -> provedeme skutečné smazání (v IO vlákně, viz deleteNote)
                deleteNote(note)
            }
            .setNegativeButton("NE") { dialog, _ ->
                // NE -> jen zavřeme dialog, nic dalšího neděláme
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteNote(note: Note) {
        // spustí se asynchronní úkol na vlákně určeném pro práci s databází
        // a ukončí se automaticky, když se aktivita zničí
        lifecycleScope.launch(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }
}