package com.example.notehub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.notehub.data.Note
import com.example.notehub.data.NoteDao
import com.example.notehub.data.NoteHubDatabaseInstance
import com.example.notehub.databinding.ActivityEditNoteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding
    private lateinit var noteDao: NoteDao
    private var noteId: Int = -1

    // poznámka načtená z databáze; potřebujeme ji při ukládání,
    // aby se nepřepsaly hodnoty, které v UI needitujeme (createdAt)
    private var currentNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteDao = NoteHubDatabaseInstance.getDatabase(this).noteDao()

        // Získáme ID poznámky z Intentu
        noteId = intent.getIntExtra("note_id", -1)

        // zobrazíme ID v TextView (binding.tvNoteId vznikne z android:id="@+id/tvNoteId")
        binding.tvNoteId.text = "ID: $noteId"

        // seznam kategorií z arrays.xml (kvůli hledání pozice ve Spinneru)
        val categories = resources.getStringArray(R.array.category_names)

        // Načteme poznámku z DB
        lifecycleScope.launch {
            noteDao.getAllNotes().collect { notes ->
                val note = notes.find { it.id == noteId }
                if (note != null) {
                    currentNote = note // zapamatujeme si celou poznámku

                    binding.etEditTitle.setText(note.title)
                    binding.etEditContent.setText(note.content)
                    binding.spinnerPriority.setSelection(note.priority)

                    // najdeme pozici kategorie v poli; když tam není, vybereme první
                    val position = categories.indexOf(note.category).coerceAtLeast(0)
                    binding.spinnerCategory.setSelection(position)
                }
            }
        }

        // Kliknutí na Uložit
        binding.btnSaveChanges.setOnClickListener {
            // Pokud se poznámka ještě nenačetla, nemáme co ukládat
            val original = currentNote ?: return@setOnClickListener

            // copy() vytvoří kopii původní poznámky a změní jen vyjmenovaná pole.
            // id i createdAt zůstanou původní.
            val updatedNote = original.copy(
                title = binding.etEditTitle.text.toString(),
                content = binding.etEditContent.text.toString(),
                priority = binding.spinnerPriority.selectedItemPosition,
                category = binding.spinnerCategory.selectedItem.toString()
            )

            lifecycleScope.launch(Dispatchers.IO) {
                noteDao.update(updatedNote)
                finish()
            }
        }

    }
}