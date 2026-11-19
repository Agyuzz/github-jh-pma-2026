package com.example.notehub



import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notehub.data.Note
import com.example.notehub.data.NoteDao
import com.example.notehub.data.NoteHubDatabaseInstance
import com.example.notehub.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
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
                showDeleteConfirmationDialog(note)
            }
        )

        // RecyclerView
        binding.recyclerViewNotes.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewNotes.adapter = adapter

        // Flow
        lifecycleScope.launch {
            noteDao.getAllNotes().collectLatest { notes ->
                adapter.submitList(notes)
            }
        }
    }
    //Potvrzovací dialog při mazání poznámky.
    private fun showDeleteConfirmationDialog(note: Note){
        AlertDialog.Builder(this)
            .setMessage ("Opravdu chcete smazat tuto poznámku?") //Text dialogu
            .setPositiveButton("Ano") { _, _ ->
                // ANO -> provedeme skutečné smazání (v IO vlákně, viz deleteNote)
                deleteNote(note)
            }
            .setNegativeButton("NE") { dialog, _ ->
                // NE -> jen zavřeme dialog, nic dalšího neděláme
                dialog.dismiss()
            }
            .show() // zobrazí dialog
    }

    private fun deleteNote(note: Note) {
        // spustí se asynchronní úkol na vlákně určeném pro práci s databází
        // a ukončí se automaticky, když se aktivita zničí
        lifecycleScope.launch(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }
}